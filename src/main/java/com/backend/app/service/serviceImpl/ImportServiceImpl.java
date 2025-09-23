package com.backend.app.service.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.CreateInvoiceItemDTO;
import com.backend.app.dto.CreateInvoiceRequestDTO;
import com.backend.app.dto.ImportErrorDTO;
import com.backend.app.dto.ImportResultDTO;
import com.backend.app.dto.InvoiceImportRowDTO;
import com.backend.app.entity.Debtor;
import com.backend.app.entity.Provider;
import com.backend.app.entity.ServiceCatalog;
import com.backend.app.enums.PayerType;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.DebtorRepository;
import com.backend.app.repository.ProviderRepository;
import com.backend.app.repository.ServiceCatalogRepository;
import com.backend.app.service.ImportService;
import com.backend.app.service.InvoiceService;

/** Service für CSV-Import und Rechnungsdaten-Massenverarbeitung */
@Service
public class ImportServiceImpl implements ImportService {

    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private DebtorRepository debtorRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    @Autowired
    private ServiceCatalogRepository serviceCatalogRepository;

    @Override
    @Transactional
    public ImportResultDTO importInvoicesFromCsv(MultipartFile file) {
        
        if (file == null || file.isEmpty()) {
            throw new ApiException("CSV file is required");
        }
        
        // Sicherheitsvalidierung
        String filename = file.getOriginalFilename();
        if (!"text/csv".equals(file.getContentType()) && 
            (filename == null || !filename.endsWith(".csv"))) {
            throw new ApiException("File must be a CSV file");
        }
        
        List<ImportErrorDTO> errors = new ArrayList<>();
        int createdCount = 0;
        int rowNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new ApiException("Empty CSV file");
            }
            
            if (!isValidCsvHeader(headerLine)) {
                throw new ApiException("Invalid CSV header. Expected: debtor_type,debtor_name,invoice_date,due_in_days,service_code,quantity,factor,surcharge_cents");
            }
            
            Provider defaultProvider = getDefaultProvider();
            
            String line;
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                
                try {
                    InvoiceImportRowDTO rowData = parseCsvRow(line, rowNumber);
                    createInvoiceFromRow(rowData, defaultProvider);
                    createdCount++;
                    
                } catch (Exception e) {
                    errors.add(new ImportErrorDTO(rowNumber, e.getMessage()));
                }
            }
            
        } catch (IOException | ApiException e) {
            if (e instanceof ApiException) {
                throw (ApiException) e;
            }
            throw new ApiException("Error processing CSV file: " + e.getMessage());
        }
        
        return new ImportResultDTO(createdCount, errors);
    }
    
    private boolean isValidCsvHeader(String header) {
        String expectedHeader = "debtor_type,debtor_name,invoice_date,due_in_days,service_code,quantity,factor,surcharge_cents";
        return expectedHeader.equals(header.trim());
    }
    
    /** CSV-Zeile mit Feld-Validierung parsen */
    private InvoiceImportRowDTO parseCsvRow(String line, int rowNumber) {
        String[] fields = line.split(",");
        
        if (fields.length != 8) {
            throw new RuntimeException("Invalid number of fields. Expected 8, got " + fields.length);
        }
        
        InvoiceImportRowDTO row = new InvoiceImportRowDTO();
        row.setRowNumber(rowNumber);
        
        try {
            // Zahlerpflicht parsen
            row.setDebtorType(PayerType.valueOf(fields[0].trim()));
            
            // Rechnungsempfänger-Name parsen
            row.setDebtorName(fields[1].trim());
            if (row.getDebtorName().isEmpty()) {
                throw new RuntimeException("debtor_name cannot be empty");
            }
            
            // Rechnungsdatum parsen
            row.setInvoiceDate(LocalDate.parse(fields[2].trim()));
            
            // Zahlungsziel in Tagen parsen
            row.setDueInDays(Integer.parseInt(fields[3].trim()));
            if (row.getDueInDays() <= 0) {
                throw new RuntimeException("due_in_days must be positive");
            }
            
            // Leistungscode parsen
            row.setServiceCode(fields[4].trim());
            if (row.getServiceCode().isEmpty()) {
                throw new RuntimeException("service_code cannot be empty");
            }
            
            // Anzahl parsen
            row.setQuantity(Integer.parseInt(fields[5].trim()));
            if (row.getQuantity() <= 0) {
                throw new RuntimeException("quantity must be positive");
            }
            
            // Steigerungsfaktor parsen
            row.setFactor(new BigDecimal(fields[6].trim()));
            if (row.getFactor().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("factor must be positive");
            }
            
            // Zuschlag in Cent parsen
            row.setSurchargeCents(Integer.parseInt(fields[7].trim()));
            if (row.getSurchargeCents() < 0) {
                throw new RuntimeException("surcharge_cents cannot be negative");
            }
            
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new RuntimeException("Invalid data format: " + e.getMessage());
        }
        
        return row;
    }
    
    private void createInvoiceFromRow(InvoiceImportRowDTO row, Provider provider) {
        
        Debtor debtor = findOrCreateDebtor(row);
        
        ServiceCatalog service = serviceCatalogRepository.findByCode(row.getServiceCode());
        if (service == null) {
            throw new RuntimeException("Unknown service_code: " + row.getServiceCode());
        }
        
        // Rechnungsanfrage erstellen
        CreateInvoiceRequestDTO invoiceRequest = new CreateInvoiceRequestDTO();
        invoiceRequest.setProviderId(provider.getId());
        invoiceRequest.setDebtorId(debtor.getId());
        invoiceRequest.setInvoiceDate(row.getInvoiceDate());
        invoiceRequest.setDueInDays(row.getDueInDays());
        
        // Rechnungsposition erstellen
        CreateInvoiceItemDTO item = new CreateInvoiceItemDTO();
        item.setServiceId(service.getId());
        item.setQuantity(row.getQuantity());
        item.setFactor(row.getFactor());
        item.setSurchargeCents(row.getSurchargeCents());
        
        invoiceRequest.setItems(List.of(item));
        
        // Rechnung erstellen
        invoiceService.createInvoice(invoiceRequest);
    }
    
    /** Rechnungsempfänger anlegen falls nicht vorhanden, Matching nach Name und Zahlerpflichtig */
    private Debtor findOrCreateDebtor(InvoiceImportRowDTO row) {
        
        String[] nameParts = row.getDebtorName().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        Debtor existingDebtor = debtorRepository.findByFirstNameAndLastName(firstName, lastName);
        
        if (existingDebtor != null && existingDebtor.getPayerType() == row.getDebtorType()) {
            return existingDebtor;
        }
        
        // Neuen Rechnungsempfänger erstellen
        Debtor newDebtor = new Debtor();
        newDebtor.setFirstName(firstName);
        newDebtor.setLastName(lastName);
        newDebtor.setPayerType(row.getDebtorType());
        // Minimal erforderliche Felder setzen
        newDebtor.setAddress("Imported - Address not provided");
        newDebtor.setEmail("imported@example.com");
        
        return debtorRepository.save(newDebtor);
    }
    
    private Provider getDefaultProvider() {
        return providerRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new ApiException("No provider available. Please create a provider first."));
    }
}
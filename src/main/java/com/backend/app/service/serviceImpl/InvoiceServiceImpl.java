package com.backend.app.service.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.app.dto.CreateInvoiceItemDTO;
import com.backend.app.dto.CreateInvoiceRequestDTO;
import com.backend.app.dto.InvoiceResponseDTO;
import com.backend.app.dto.InvoiceSummaryDTO;
import com.backend.app.dto.UpdateInvoiceRequestDTO;
import com.backend.app.entity.Debtor;
import com.backend.app.entity.Invoice;
import com.backend.app.entity.InvoiceItem;
import com.backend.app.entity.Provider;
import com.backend.app.entity.ServiceCatalog;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.DebtorRepository;
import com.backend.app.repository.InvoiceRepository;
import com.backend.app.repository.ProviderRepository;
import com.backend.app.repository.ServiceCatalogRepository;
import com.backend.app.service.InvoiceService;

/**
 * Service für Rechnungserstellung und -verwaltung
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private DebtorRepository debtorRepository;

    @Autowired
    private ServiceCatalogRepository serviceCatalogRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO) {

        // Eingabe validieren
        if (createInvoiceRequestDTO.getItems() == null || createInvoiceRequestDTO.getItems().isEmpty()) {
            throw new ApiException("Invoice must contain at least one item.");
        }

        // Abrechnungspartner laden
        Provider provider = providerRepository.findById(createInvoiceRequestDTO.getProviderId())
                .orElseThrow(() -> new ApiException("Provider with id " + createInvoiceRequestDTO.getProviderId() + " not found"));

        // Rechnungsempfänger laden  
        Debtor debtor = debtorRepository.findById(createInvoiceRequestDTO.getDebtorId())
                .orElseThrow(() -> new ApiException("Debtor with id " + createInvoiceRequestDTO.getDebtorId() + " not found"));

        // Rechnung erstellen
        Invoice invoice = new Invoice();
        invoice.setProvider(provider);
        invoice.setDebtor(debtor);
        invoice.setInvoiceDate(createInvoiceRequestDTO.getInvoiceDate());

        // Fälligkeitsdatum berechnen
        LocalDate dueDate = createInvoiceRequestDTO.getInvoiceDate()
                .plusDays(createInvoiceRequestDTO.getDueInDays() != null ? createInvoiceRequestDTO.getDueInDays() : 14);
        invoice.setDueDate(dueDate);

        // dueDate >= invoiceDate
        if (dueDate.isBefore(createInvoiceRequestDTO.getInvoiceDate())) {
            throw new ApiException("Due date must be on or after invoice date");
        }

        invoice.setStatus(InvoiceStatus.OPEN);

        // Create Invoice Items
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        int totalCents = 0;

        for (CreateInvoiceItemDTO itemDTO : createInvoiceRequestDTO.getItems()) {
            // quantity >= 1
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() < 1) {
                throw new ApiException("Quantity must be >= 1");
            }

            //factor >= 1.0
            if (itemDTO.getFactor() == null || itemDTO.getFactor().doubleValue() < 1.0) {
                throw new ApiException("Factor must be >= 1.0");
            }

            // Get Service
            ServiceCatalog service = serviceCatalogRepository.findById(itemDTO.getServiceId())
                    .orElseThrow(() -> new ApiException("Service with id " + itemDTO.getServiceId() + " not found"));

            // Create Invoice Item
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setService(service);
            item.setQuantity(itemDTO.getQuantity());
            item.setFactor(itemDTO.getFactor());
            item.setUnitPriceCents(service.getBaseFeeCents()); // snapshot from service
            item.setSurchargeCents(itemDTO.getSurchargeCents() != null ? itemDTO.getSurchargeCents() : 0);

            //line total: round(unit*factor) * quantity + surcharge
            int roundedUnitPrice = (int) Math.round(service.getBaseFeeCents() * itemDTO.getFactor().doubleValue());
            int lineTotal = roundedUnitPrice * itemDTO.getQuantity() + item.getSurchargeCents();
            item.setLineTotalCents(lineTotal);

            invoiceItems.add(item);
            totalCents += lineTotal;
        }

        invoice.setItems(invoiceItems);
        invoice.setTotalCents(totalCents);
        invoice.setPaidCents(0); // Always start with 0 paid
        invoice.setDunningLevel(com.backend.app.enums.DunningLevel.NONE); // Always start with NONE

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return modelMapper.map(savedInvoice, InvoiceResponseDTO.class);
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Invoice with id " + id + " not found"));
        return modelMapper.map(invoice, InvoiceResponseDTO.class);
    }

    @Override
    public Page<InvoiceSummaryDTO> getAllInvoices(
            InvoiceStatus status,
            DunningLevel dunningLevel,
            LocalDate dateFrom,
            LocalDate dateTo,
            String providerName,
            String debtorName,
            Pageable pageable) {

        Page<Invoice> invoices;

        // Use simple Spring Data JPA methods based on provided parameters
        if (status != null && dunningLevel != null && dateFrom != null && dateTo != null) {
            invoices = invoiceRepository.findByStatusAndDunningLevelAndInvoiceDateBetween(
                    status, dunningLevel, dateFrom, dateTo, pageable);
        } else if (status != null && dateFrom != null && dateTo != null) {
            invoices = invoiceRepository.findByStatusAndInvoiceDateBetween(
                    status, dateFrom, dateTo, pageable);
        } else if (dunningLevel != null && dateFrom != null && dateTo != null) {
            invoices = invoiceRepository.findByDunningLevelAndInvoiceDateBetween(
                    dunningLevel, dateFrom, dateTo, pageable);
        } else if (status != null && dunningLevel != null) {
            invoices = invoiceRepository.findByStatusAndDunningLevel(status, dunningLevel, pageable);
        } else if (dateFrom != null && dateTo != null) {
            invoices = invoiceRepository.findByInvoiceDateBetween(dateFrom, dateTo, pageable);
        } else if (status != null) {
            invoices = invoiceRepository.findByStatus(status, pageable);
        } else if (dunningLevel != null) {
            invoices = invoiceRepository.findByDunningLevel(dunningLevel, pageable);
        } else {
            // No filters - get all invoices
            invoices = invoiceRepository.findAll(pageable);
        }

        return invoices.map(this::convertToSummaryDTO);
    }

    @Override
    public InvoiceResponseDTO updateInvoice(Long id, UpdateInvoiceRequestDTO updateRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Invoice with id " + id + " not found"));

        if (invoice.getDunningLevel() != DunningLevel.NONE) {
            throw new ApiException("Cannot update invoice that has been dunned (dunning level: " + invoice.getDunningLevel() + ")");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ApiException("Cannot update paid invoices");
        }

        // dueDate >= invoiceDate
        if (updateRequest.getDueDate().isBefore(invoice.getInvoiceDate())) {
            throw new ApiException("Due date cannot be before invoice date");
        }

        invoice.setDueDate(updateRequest.getDueDate());
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return modelMapper.map(savedInvoice, InvoiceResponseDTO.class);
    }

    @Override
    public InvoiceResponseDTO cancelInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Invoice with id " + id + " not found"));

        if (invoice.getPaidCents() > 0) {
            throw new ApiException("Cannot cancel invoice with payments (paidCents: " + invoice.getPaidCents() + ")");
        }

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ApiException("Invoice is already cancelled");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return modelMapper.map(savedInvoice, InvoiceResponseDTO.class);
    }

    private InvoiceSummaryDTO convertToSummaryDTO(Invoice invoice) {
        InvoiceSummaryDTO dto = new InvoiceSummaryDTO();
        dto.setId(invoice.getId());
        dto.setProviderName(invoice.getProvider().getName());
        dto.setDebtorDisplay(invoice.getDebtor().getFirstName() + " " + invoice.getDebtor().getLastName());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setStatus(invoice.getStatus());
        dto.setDunningLevel(invoice.getDunningLevel());
        dto.setTotalCents(invoice.getTotalCents());
        dto.setPaidCents(invoice.getPaidCents());
        dto.setOpenCents(invoice.getOpenCents());
        return dto;
    }
}

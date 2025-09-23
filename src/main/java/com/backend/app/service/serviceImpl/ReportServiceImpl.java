package com.backend.app.service.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.app.entity.Invoice;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.repository.InvoiceRepository;
import com.backend.app.service.ReportService;

/**
 * Service für CSV-Export und Rechnungsberichte
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public String generateInvoicesReport(InvoiceStatus status, DunningLevel dunningLevel,
            LocalDate dateFrom, LocalDate dateTo, String providerName, String debtorName) {

        // Alle passenden Datensätze exportieren mit gleichen Filtern wie Rechnungslisten-API
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Invoice> invoicePage;

        // Use same filtering logic as InvoiceServiceImpl
        if (status != null && dunningLevel != null && dateFrom != null && dateTo != null) {
            invoicePage = invoiceRepository.findByStatusAndDunningLevelAndInvoiceDateBetween(
                    status, dunningLevel, dateFrom, dateTo, pageable);
        } else if (status != null && dateFrom != null && dateTo != null) {
            invoicePage = invoiceRepository.findByStatusAndInvoiceDateBetween(
                    status, dateFrom, dateTo, pageable);
        } else if (dunningLevel != null && dateFrom != null && dateTo != null) {
            invoicePage = invoiceRepository.findByDunningLevelAndInvoiceDateBetween(
                    dunningLevel, dateFrom, dateTo, pageable);
        } else if (status != null && dunningLevel != null) {
            invoicePage = invoiceRepository.findByStatusAndDunningLevel(status, dunningLevel, pageable);
        } else if (dateFrom != null && dateTo != null) {
            invoicePage = invoiceRepository.findByInvoiceDateBetween(dateFrom, dateTo, pageable);
        } else if (status != null) {
            invoicePage = invoiceRepository.findByStatus(status, pageable);
        } else if (dunningLevel != null) {
            invoicePage = invoiceRepository.findByDunningLevel(dunningLevel, pageable);
        } else {
            // No filters - get all invoices
            invoicePage = invoiceRepository.findAll(pageable);
        }

        return generateCsvFromInvoices(invoicePage.getContent());
    }

    /**
     * Rechnungsdaten in CSV-Format konvertieren
     */
    private String generateCsvFromInvoices(List<Invoice> invoices) {
        StringBuilder csv = new StringBuilder();

        csv.append("invoice_id,provider,debtor,invoice_date,due_date,status,dunning,total_cents,paid_cents,open_cents\n");
        for (Invoice invoice : invoices) {
            csv.append(invoice.getId()).append(",")
                    .append(escapeCsvField(getProviderName(invoice))).append(",")
                    .append(escapeCsvField(getDebtorName(invoice))).append(",")
                    .append(invoice.getInvoiceDate()).append(",")
                    .append(invoice.getDueDate()).append(",")
                    .append(invoice.getStatus()).append(",")
                    .append(invoice.getDunningLevel()).append(",")
                    .append(invoice.getTotalCents()).append(",")
                    .append(invoice.getPaidCents()).append(",")
                    .append(invoice.getOpenCents()).append("\n");
        }

        return csv.toString();
    }

    private String getProviderName(Invoice invoice) {
        if (invoice.getProvider() != null) {
            return invoice.getProvider().getName();
        }
        return "Unknown Provider";
    }

    private String getDebtorName(Invoice invoice) {
        if (invoice.getDebtor() != null) {
            return invoice.getDebtor().getFirstName() + " "
                    + invoice.getDebtor().getLastName();
        }
        return "Unknown Debtor";
    }

    /**
     * CSV-Felder mit Kommas oder Anführungszeichen escapen
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }

        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}

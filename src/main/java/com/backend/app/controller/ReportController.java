package com.backend.app.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /** Rechnungsdaten als CSV-Export für Buchführung und Controlling */
    @GetMapping(value = "/invoices.csv", produces = "text/csv")
    public ResponseEntity<String> exportInvoices(
            @RequestParam(value = "status", required = false) InvoiceStatus status,
            @RequestParam(value = "dunning_level", required = false) DunningLevel dunningLevel,
            @RequestParam(value = "date_from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "date_to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(value = "provider_name", required = false) String providerName,
            @RequestParam(value = "debtor_name", required = false) String debtorName) {
        
        String csvContent = reportService.generateInvoicesReport(status, dunningLevel, dateFrom, dateTo, providerName, debtorName);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"invoices_export.csv\"");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
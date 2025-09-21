package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Invoice Summary DTO
 * 
 * Zweck: Übersichtsliste aller Rechnungen für Worklists und Reports.
 * Verwendung: GET /api/v1/invoices (gefiltert/paginiert) 
 * 
 * Funktionen:
 * - Kompakte Rechnungsübersicht ohne Items/Payments
 * - Status-Filter für offene/bezahlte/stornierte Rechnungen
 * - Mahnstand-Anzeige für Inkasso-Workflows
 * - Performance-optimiert durch Projektion statt Full-Entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSummaryDTO {
    
    private Long id;
    
    private String providerName;
    
    private String debtorDisplay;
    
    private LocalDate invoiceDate;
    
    private LocalDate dueDate;
    
    private InvoiceStatus status;
    
    private DunningLevel dunningLevel;
    
    private Integer totalCents;
    
    private Integer paidCents;
    
    private Integer openCents;
}
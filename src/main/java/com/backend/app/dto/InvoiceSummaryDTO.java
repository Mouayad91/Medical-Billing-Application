package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Kompakte Rechnungsübersicht für Listen, Workflows und Reports */
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
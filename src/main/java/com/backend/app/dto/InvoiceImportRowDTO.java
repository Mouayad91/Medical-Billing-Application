package com.backend.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

@Data
public class InvoiceImportRowDTO {
    private PayerType debtorType;
    private String debtorName;
    private LocalDate invoiceDate;
    private Integer dueInDays;
    private String serviceCode;
    private Integer quantity;
    private BigDecimal factor;
    private Integer surchargeCents;
    
    // Additional fields for processing
    private int rowNumber;
}
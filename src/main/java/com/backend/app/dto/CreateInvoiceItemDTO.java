package com.backend.app.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Teil des CreateInvoiceRequestDTO für POST /invoices 
 * Zweck: Einzelne Rechnungsposition mit Leistung, Menge, Faktor und Zuschlägen
 * Verwendet von: ROLE_BILLING, ROLE_ADMIN (Teil der Rechnungserstellung)
 */
@Data
public class CreateInvoiceItemDTO {
    private Long serviceId;
    private Integer quantity;
    private BigDecimal factor;
    private Integer surchargeCents;
}
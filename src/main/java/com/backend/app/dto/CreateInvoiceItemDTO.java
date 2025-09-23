package com.backend.app.dto;

import java.math.BigDecimal;

import lombok.Data;

/** Einzelne Rechnungsposition mit Leistung, Menge und Preisanpassungen */
@Data
public class CreateInvoiceItemDTO {
    private Long serviceId;
    private Integer quantity;
    private BigDecimal factor;
    private Integer surchargeCents;
}
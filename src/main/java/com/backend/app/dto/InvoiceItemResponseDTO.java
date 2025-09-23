package com.backend.app.dto;

import java.math.BigDecimal;

import lombok.Data;

/** Einzelne Rechnungsposition mit Leistungsdetails und berechneten Preisen */
@Data
public class InvoiceItemResponseDTO {
    private Long id;
    private Long serviceId;
    private String serviceCode;
    private String description;
    private Integer quantity;
    private BigDecimal factor;
    private Integer unitPriceCents;
    private Integer surchargeCents;
    private Integer lineTotalCents;
}
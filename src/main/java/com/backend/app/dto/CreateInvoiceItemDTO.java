package com.backend.app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateInvoiceItemDTO {
    private Long serviceId;
    private Integer quantity;
    private BigDecimal factor;
    private Integer surchargeCents;
}
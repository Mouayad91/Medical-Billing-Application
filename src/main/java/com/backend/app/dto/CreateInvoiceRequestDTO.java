package com.backend.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/**
 * Request DTO für POST /invoices
 * Zweck: Rechnung erstellen (Provider + Debtor + Items) - Kern der Privatliquidation
 * Verwendet von: ROLE_BILLING, ROLE_ADMIN
 */
@Data
public class CreateInvoiceRequestDTO {
    private Long providerId;
    private Long debtorId;
    private LocalDate invoiceDate;
    private Integer dueInDays;
    private List<CreateInvoiceItemDTO> items;
}
package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import lombok.Data;

/**
 * Request DTO für POST /invoices/{id}/payments
 * Zweck: Zahlung zu Rechnung hinzufügen - Kern des Forderungsmanagements mit Statusaktualisierung  
 * Verwendet von: ROLE_COLLECTIONS, ROLE_BILLING, ROLE_ADMIN
 */
@Data
public class AddPaymentRequestDTO {
    private Integer amountCents;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private String note;
}
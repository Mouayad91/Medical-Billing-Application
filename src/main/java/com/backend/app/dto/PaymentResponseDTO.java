package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO für POST /invoices/{id}/payments
 * Zweck: Antwort nach erfolgreicher Zahlungsbuchung
 * Verwendet von: ROLE_COLLECTIONS, ROLE_BILLING, ROLE_ADMIN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Integer amountCents;
    private PaymentMethod method;
    private LocalDate paymentDate;
    private String note;
}
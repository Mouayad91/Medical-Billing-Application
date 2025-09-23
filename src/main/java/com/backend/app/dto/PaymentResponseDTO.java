package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Bestätigung eines erfassten Zahlungseingangs mit Referenzdaten */
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
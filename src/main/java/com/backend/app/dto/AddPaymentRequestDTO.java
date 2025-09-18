package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import lombok.Data;

@Data
public class AddPaymentRequestDTO {
    private Integer amountCents;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private String note;
}
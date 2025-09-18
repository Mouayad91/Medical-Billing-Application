package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import lombok.Data;

@Data
public class PaymentSummaryDTO {
    private Long id;
    private Integer amountCents;
    private PaymentMethod method;
    private LocalDate paymentDate;
    private String note;
}
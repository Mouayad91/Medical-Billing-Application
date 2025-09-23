package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PaymentMethod;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Zahlungseingang zu bestehender Rechnung mit Validierung erfassen */
@Data
public class AddPaymentRequestDTO {
    
    @NotNull(message = "amountCents is required")
    @Min(value = 1, message = "amountCents must be greater than 0")
    private Integer amountCents;
    
    @NotNull(message = "paymentDate is required")
    private LocalDate paymentDate;
    
    @NotNull(message = "method is required")
    private PaymentMethod method;
    
    private String note;
}
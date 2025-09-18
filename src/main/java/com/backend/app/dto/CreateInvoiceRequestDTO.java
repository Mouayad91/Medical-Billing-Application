package com.backend.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CreateInvoiceRequestDTO {
    private Long providerId;
    private Long patientId;
    private LocalDate invoiceDate;
    private Integer dueInDays;
    private List<CreateInvoiceItemDTO> items;
}
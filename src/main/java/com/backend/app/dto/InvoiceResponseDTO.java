package com.backend.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

import lombok.Data;

@Data
public class InvoiceResponseDTO {
    private Long id;
    private String providerName;
    private String patientDisplay;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private DunningLevel dunningLevel;
    private Integer totalCents;
    private Integer paidCents;
    private Integer openCents;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponseDTO> items;
    private List<PaymentSummaryDTO> payments;
    private List<AttachmentResponseDTO> attachments;
}
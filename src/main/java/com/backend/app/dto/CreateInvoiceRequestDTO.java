package com.backend.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/** Neue Rechnung mit Leistungspositionen und Fälligkeitsdaten erstellen */
@Data
public class CreateInvoiceRequestDTO {
    private Long providerId;
    private Long debtorId;
    private LocalDate invoiceDate;
    private Integer dueInDays;
    private List<CreateInvoiceItemDTO> items;
}
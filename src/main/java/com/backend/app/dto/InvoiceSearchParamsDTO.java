package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

import lombok.Data;

@Data
public class InvoiceSearchParamsDTO {
    private InvoiceStatus status;
    private DunningLevel dunning;
    private LocalDate from;
    private LocalDate to;
    private String providerName;
    private String debtorName;
    private Integer page;
    private Integer size;
    private String sort;
}
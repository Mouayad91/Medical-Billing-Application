package com.backend.app.dto;

import java.time.Instant;

import lombok.Data;

/** Leistung aus dem Katalog mit Preisen für die Rechnungserstellung */
@Data
public class ServiceResponseDTO {
    private Long id;
    private String code;
    private String description;
    private Integer baseFeeCents;
    private Instant createdAt;
}
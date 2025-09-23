package com.backend.app.dto;

import lombok.Data;

/** Neue abrechenbare Leistung mit Code und Grundpreis für Katalog anlegen */
@Data
public class CreateServiceRequestDTO {
    private String code;
    private String description;
    private Integer baseFeeCents;
}
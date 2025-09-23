package com.backend.app.dto;

import java.time.Instant;

import com.backend.app.enums.ProviderType;

import lombok.Data;

/** Abrechnungspartner-Stammdaten für Auswahl und Rechnungszuordnung */
@Data
public class ProviderResponseDTO {
    private Long id;
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
    private Instant createdAt;
}
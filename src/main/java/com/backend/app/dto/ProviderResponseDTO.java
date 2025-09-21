package com.backend.app.dto;

import java.time.Instant;

import com.backend.app.enums.ProviderType;

import lombok.Data;

/**
 * Response DTO für GET /providers, GET /providers/{id}
 * Zweck: Abrechnungspartner-Daten zurückgeben für Suche, Einzelansicht und Rechnungserstellung
 * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
 */
@Data
public class ProviderResponseDTO {
    private Long id;
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
    private Instant createdAt;
}
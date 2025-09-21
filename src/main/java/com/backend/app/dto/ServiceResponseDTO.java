package com.backend.app.dto;

import java.time.Instant;

import lombok.Data;

/**
 * Response DTO für GET /services, GET /services/{id}
 * Zweck: Leistungskatalog-Daten für Suche, Einzelansicht und Rechnungserstellung
 * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
 */
@Data
public class ServiceResponseDTO {
    private Long id;
    private String code;
    private String description;
    private Integer baseFeeCents;
    private Instant createdAt;
}
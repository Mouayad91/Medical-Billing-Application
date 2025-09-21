package com.backend.app.dto;

import lombok.Data;

/**
 * Request DTO für POST /services
 * Zweck: Abrechenbare Leistung (GOÄ-ähnlich) anlegen - dient als Preisquelle für Rechnungspositionen
 * Verwendet von: ROLE_BILLING, ROLE_ADMIN
 */
@Data
public class CreateServiceRequestDTO {
    private String code;
    private String description;
    private Integer baseFeeCents;
}
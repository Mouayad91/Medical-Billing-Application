package com.backend.app.dto;

import lombok.Data;

/**
 * Response DTO für POST /dunning/run
 * Zweck: Ergebnis des Mahnlaufs - wie viele Rechnungen aktualisiert wurden
 * Verwendet von: ROLE_COLLECTIONS, ROLE_ADMIN
 */
@Data
public class DunningRunResponseDTO {
    private Integer updatedCount;
}
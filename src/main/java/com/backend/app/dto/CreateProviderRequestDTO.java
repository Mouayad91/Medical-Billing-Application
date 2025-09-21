package com.backend.app.dto;

import com.backend.app.enums.ProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/**
 * Request DTO für POST /providers
 * Zweck: Abrechnungspartner (Ärzte/Praxen/Kliniken) anlegen - ohne Provider keine Rechnung möglich
 * Verwendet von: ROLE_BILLING, ROLE_ADMIN
 */

public class CreateProviderRequestDTO {
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
}
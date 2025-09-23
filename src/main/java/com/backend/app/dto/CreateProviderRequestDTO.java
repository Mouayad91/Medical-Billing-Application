package com.backend.app.dto;

import com.backend.app.enums.ProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/** Neuen Abrechnungspartner (Arzt/Praxis/Klinik) mit Steuerdaten anlegen */

public class CreateProviderRequestDTO {
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
}
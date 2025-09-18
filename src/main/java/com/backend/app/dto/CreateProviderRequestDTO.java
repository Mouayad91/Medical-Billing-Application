package com.backend.app.dto;

import com.backend.app.enums.ProviderType;

import lombok.Data;

@Data
public class CreateProviderRequestDTO {
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
}
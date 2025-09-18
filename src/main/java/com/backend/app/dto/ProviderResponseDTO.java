package com.backend.app.dto;

import java.time.LocalDateTime;

import com.backend.app.enums.ProviderType;

import lombok.Data;

@Data
public class ProviderResponseDTO {
    private Long id;
    private String name;
    private ProviderType type;
    private String specialty;
    private String taxId;
    private LocalDateTime createdAt;
}
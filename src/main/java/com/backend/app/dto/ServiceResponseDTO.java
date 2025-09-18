package com.backend.app.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ServiceResponseDTO {
    private Long id;
    private String code;
    private String description;
    private Integer baseFeeCents;
    private LocalDateTime createdAt;
}
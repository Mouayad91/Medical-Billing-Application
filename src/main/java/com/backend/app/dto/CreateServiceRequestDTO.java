package com.backend.app.dto;

import lombok.Data;

@Data
public class CreateServiceRequestDTO {
    private String code;
    private String description;
    private Integer baseFeeCents;
}
package com.backend.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.app.enums.PayerType;

import lombok.Data;

@Data
public class PatientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String address;
    private PayerType payerType;
    private String payerDetails;
    private LocalDateTime createdAt;
}
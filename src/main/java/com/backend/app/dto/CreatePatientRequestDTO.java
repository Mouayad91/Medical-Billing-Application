package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

@Data
public class CreatePatientRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String address;
    private PayerType payerType;
    private String payerDetails;
}
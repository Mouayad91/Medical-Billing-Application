package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

/** Neuen Rechnungsempfänger mit Adress- und Zahlerdaten anlegen */
@Data
public class CreateDebtorRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String address;
    private PayerType payerType;
    private String payerDetails;
}
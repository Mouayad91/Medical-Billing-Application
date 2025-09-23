package com.backend.app.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

/** Rechnungsempfänger-Stammdaten für Auswahl und Rechnungszuordnung */
@Data
public class DebtorResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String address;
    private PayerType payerType;
    private String payerDetails;
    private Instant createdAt;
}
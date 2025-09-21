package com.backend.app.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

/**
 * Response DTO für GET /debtors, GET /debtors/{id}
 * Zweck: Rechnungsempfänger-Daten für Suche, Einzelansicht und Rechnungserstellung  
 * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
 */
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
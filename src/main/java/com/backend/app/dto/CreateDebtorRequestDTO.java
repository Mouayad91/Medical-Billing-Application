package com.backend.app.dto;

import java.time.LocalDate;

import com.backend.app.enums.PayerType;

import lombok.Data;

/**
 * Request DTO für POST /debtors 
 * Zweck: Rechnungsempfänger (Zahler/Patient/Versicherung) anlegen - ohne Debtor keine Rechnung möglich
 * Verwendet von: ROLE_BILLING, ROLE_ADMIN
 */
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
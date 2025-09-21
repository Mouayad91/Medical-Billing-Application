package com.backend.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Invoice Request DTO
 * 
 * Zweck: Rechnungsänderung (z.B. Fälligkeitsdatum korrigieren vor Mahnung).
 * Verwendung: PATCH /api/v1/invoices/{id}
 * 
 * Geschäftsregeln:
 * - Nur unbezahlte oder teilbezahlte Rechnungen änderbar
 * - Fälligkeitsdatum nicht vor Rechnungsdatum
 * - Änderung nach Mahnlauf meist gesperrt (409 Conflict)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequestDTO {
    
    @NotNull
    private LocalDate dueDate;
}
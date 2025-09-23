package com.backend.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Administrative Rechnungskorrektur vor Mahnverfahren (z.B. Fälligkeitsdatum) */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequestDTO {
    
    @NotNull
    private LocalDate dueDate;
}
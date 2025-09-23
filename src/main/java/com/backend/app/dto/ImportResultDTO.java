package com.backend.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Ergebnis des CSV-Rechnungsimports mit Erfolgs- und Fehlerzählung */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDTO {
    private Integer createdCount;
    private List<ImportErrorDTO> errors;
}
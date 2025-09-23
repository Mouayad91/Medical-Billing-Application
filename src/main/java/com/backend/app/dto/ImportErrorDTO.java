package com.backend.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Einzelfehler beim CSV-Import mit Zeilennummer und Fehlermeldung */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDTO {
    private Integer row;
    private String message;
}
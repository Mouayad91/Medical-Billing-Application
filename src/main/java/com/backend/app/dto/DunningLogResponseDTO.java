package com.backend.app.dto;

import java.time.Instant;

import com.backend.app.enums.DunningLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Mahnhistorie-Eintrag mit Eskalationsstufe und Zeitstempel */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DunningLogResponseDTO {
    private Long id;
    private DunningLevel level;
    private Instant loggedAt;
    private String note;
}
package com.backend.app.dto;

import lombok.Data;
import java.util.List;

/** Paginierte Antwort für Abrechnungspartner-Listen mit Navigation und Sortierung */
@Data
public class ProviderPageResponseDTO {
    private List<ProviderResponseDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private String sortBy;
    private String sortDir;
}
package com.backend.app.dto;

import lombok.Data;
import java.util.List;

/** Paginierte Antwort für Leistungskatalog-Listen mit Suchfunktion und Navigation */
@Data
public class ServicePageResponseDTO {
    private List<ServiceResponseDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private String sortBy;
    private String sortDir;
}
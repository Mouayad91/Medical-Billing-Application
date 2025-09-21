package com.backend.app.dto;

import lombok.Data;
import java.util.List;

/**
 * Paginated Response DTO für GET /providers mit Paginierung
 * Zweck: Seitenweise Provider-Auflistung mit Metadaten für Navigation
 * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
 */
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
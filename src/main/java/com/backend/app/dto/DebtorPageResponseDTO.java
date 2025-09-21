package com.backend.app.dto;

import lombok.Data;
import java.util.List;

/**
 * Paginated Response DTO für GET /debtors mit Paginierung
 * Zweck: Seitenweise Schuldner-Auflistung mit Metadaten für Navigation
 * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
 */
@Data
public class DebtorPageResponseDTO {
    private List<DebtorResponseDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private String sortBy;
    private String sortDir;
}
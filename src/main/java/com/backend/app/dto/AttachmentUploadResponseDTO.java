package com.backend.app.dto;

import java.time.Instant;

import lombok.Data;

/**
 * Response DTO für POST /invoices/{id}/attachments  
 * Zweck: Bestätigung des Dokumenten-Uploads mit Download-URL - Korrespondenz-Management
 * Verwendet von: ROLE_BILLING, ROLE_COLLECTIONS, ROLE_ADMIN
 */
@Data
public class AttachmentUploadResponseDTO {
    private Long id;
    private String filename;
    private String contentType;
    private String downloadUrl;
    private Instant uploadedAt;
}
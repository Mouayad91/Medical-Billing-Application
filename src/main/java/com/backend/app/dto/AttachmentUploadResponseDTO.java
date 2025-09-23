package com.backend.app.dto;

import java.time.Instant;

import lombok.Data;

/** Upload-Bestätigung mit Dokument-ID und Download-Link für Archivierung */
@Data
public class AttachmentUploadResponseDTO {
    private Long id;
    private String filename;
    private String contentType;
    private String downloadUrl;
    private Instant uploadedAt;
}
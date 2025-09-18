package com.backend.app.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AttachmentUploadResponseDTO {
    private Long id;
    private String filename;
    private String contentType;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}
package com.backend.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.AttachmentResponseDTO;
import com.backend.app.dto.AttachmentUploadResponseDTO;
import com.backend.app.service.AttachmentService;

@RestController
@RequestMapping("/api/v1")
public class AttachmentController {
    
    @Autowired
    private AttachmentService attachmentService;

    /** Dokument zu Rechnung hochladen für Korrespondenz-Archivierung */
    @PostMapping(value = "/invoices/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentUploadResponseDTO> uploadAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        
        AttachmentUploadResponseDTO response = attachmentService.uploadAttachment(id, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /** Alle Dokumente der Rechnung zur Übersicht auflisten */
    @GetMapping("/invoices/{id}/attachments")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByInvoice(@PathVariable Long id) {
        
        List<AttachmentResponseDTO> attachments = attachmentService.getAttachmentsByInvoiceId(id);
        return ResponseEntity.ok(attachments);
    }

    /** Dokument-Download mit korrekten Content-Type Headers */
    @GetMapping("/invoices/{id}/attachments/{attId}")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long id, 
            @PathVariable Long attId) {
        
        Resource resource = attachmentService.downloadAttachment(id, attId);
        AttachmentResponseDTO attachment = attachmentService.getAttachmentById(id, attId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"");
        
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (attachment.getContentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(attachment.getContentType());
            } catch (Exception e) {
                // Fallback to octet-stream
            }
        }
        
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(mediaType)
            .body(resource);
    }

    /** Dokument aus Rechnung entfernen bei Archiv-Bereinigung */
    @DeleteMapping("/invoices/{id}/attachments/{attId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long id, 
            @PathVariable Long attId) {
        
        attachmentService.deleteAttachment(id, attId);
        return ResponseEntity.noContent().build();
    }
}
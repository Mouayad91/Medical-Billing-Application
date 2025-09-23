package com.backend.app.service.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.AttachmentResponseDTO;
import com.backend.app.dto.AttachmentUploadResponseDTO;
import com.backend.app.entity.Attachment;
import com.backend.app.entity.Invoice;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.AttachmentRepository;
import com.backend.app.repository.InvoiceRepository;
import com.backend.app.service.AttachmentService;

/** Service für Dokumenten-Upload und Rechnungsanhänge */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    // Sicherheits-Whitelist für Datei-Uploads
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "application/pdf",
        "image/jpeg", 
        "image/png",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain"
    );
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    @Override
    @Transactional
    public AttachmentUploadResponseDTO uploadAttachment(Long invoiceId, MultipartFile file) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ApiException("Invoice not found with id: " + invoiceId));
        
        if (file == null || file.isEmpty()) {
            throw new ApiException("File is required");
        }
        
        // Sicherheitsvalidierung
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException("File size exceeds maximum limit of 10MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ApiException("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_CONTENT_TYPES));
        }
        
        // Dateiname zur Sicherheit bereinigen
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new ApiException("Filename is required");
        }
        originalFilename = StringUtils.cleanPath(originalFilename);
        if (originalFilename.contains("..")) {
            throw new ApiException("Invalid filename");
        }
        
        try {
            // Upload-Verzeichnis sicherstellen
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Eindeutigen Dateinamen generieren um Konflikte zu vermeiden
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            Path targetPath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Metadaten in Datenbank speichern
            Attachment attachment = new Attachment();
            attachment.setInvoice(invoice);
            attachment.setFilename(originalFilename);
            attachment.setContentType(contentType);
            attachment.setStoragePath(targetPath.toString());
            
            attachment = attachmentRepository.save(attachment);
            
            // Antwort mit Download-URL erstellen
            AttachmentUploadResponseDTO response = new AttachmentUploadResponseDTO();
            response.setId(attachment.getId());
            response.setFilename(attachment.getFilename());
            response.setContentType(attachment.getContentType());
            response.setDownloadUrl("/api/v1/invoices/" + invoiceId + "/attachments/" + attachment.getId());
            response.setUploadedAt(attachment.getUploadedAt());
            
            return response;
            
        } catch (IOException e) {
            throw new ApiException("Failed to store file: " + e.getMessage());
        }
    }
    
    @Override
    public List<AttachmentResponseDTO> getAttachmentsByInvoiceId(Long invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new ApiException("Invoice not found with id: " + invoiceId);
        }
        
        List<Attachment> attachments = attachmentRepository.findByInvoiceIdOrderByUploadedAtDesc(invoiceId);
        
        return attachments.stream()
            .map(attachment -> mapToResponseDTO(attachment, invoiceId))
            .collect(Collectors.toList());
    }
    
    @Override
    public Resource downloadAttachment(Long invoiceId, Long attachmentId) {
        // Anhang finden und sicherstellen, dass er zur angegebenen Rechnung gehört (Sicherheitsprüfung)
        Attachment attachment = attachmentRepository.findByIdAndInvoiceId(attachmentId, invoiceId)
            .orElseThrow(() -> new ApiException("Attachment not found"));
        
        try {
            Path filePath = Paths.get(attachment.getStoragePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ApiException("File not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new ApiException("File path error: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteAttachment(Long invoiceId, Long attachmentId) {
        // Anhang finden und sicherstellen, dass er zur angegebenen Rechnung gehört
        Attachment attachment = attachmentRepository.findByIdAndInvoiceId(attachmentId, invoiceId)
            .orElseThrow(() -> new ApiException("Attachment not found"));
        
        try {
            // Physische Datei löschen
            Path filePath = Paths.get(attachment.getStoragePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // Datenbankdatensatz löschen
            attachmentRepository.delete(attachment);
            
        } catch (IOException e) {
            // Fehler protokollieren, aber trotzdem Datenbankdatensatz löschen
            attachmentRepository.delete(attachment);
            throw new ApiException("File deleted from database but physical file deletion failed: " + e.getMessage());
        }
    }
    
    @Override
    public AttachmentResponseDTO getAttachmentById(Long invoiceId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findByIdAndInvoiceId(attachmentId, invoiceId)
            .orElseThrow(() -> new ApiException("Attachment not found"));
        
        return mapToResponseDTO(attachment, invoiceId);
    }
    
    private AttachmentResponseDTO mapToResponseDTO(Attachment attachment, Long invoiceId) {
        AttachmentResponseDTO dto = new AttachmentResponseDTO();
        dto.setId(attachment.getId());
        dto.setFilename(attachment.getFilename());
        dto.setContentType(attachment.getContentType());
        dto.setDownloadUrl("/api/v1/invoices/" + invoiceId + "/attachments/" + attachment.getId());
        
        // Instant zu LocalDateTime für Antwort konvertieren
        if (attachment.getUploadedAt() != null) {
            dto.setUploadedAt(LocalDateTime.ofInstant(attachment.getUploadedAt(), ZoneId.systemDefault()));
        }
        
        return dto;
    }
}
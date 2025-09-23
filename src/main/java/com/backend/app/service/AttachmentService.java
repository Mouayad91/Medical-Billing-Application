package com.backend.app.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.AttachmentResponseDTO;
import com.backend.app.dto.AttachmentUploadResponseDTO;

@Service
public interface AttachmentService {
    
    AttachmentUploadResponseDTO uploadAttachment(Long invoiceId, MultipartFile file);
    
    List<AttachmentResponseDTO> getAttachmentsByInvoiceId(Long invoiceId);
    
    Resource downloadAttachment(Long invoiceId, Long attachmentId);
    
    void deleteAttachment(Long invoiceId, Long attachmentId);
    
    AttachmentResponseDTO getAttachmentById(Long invoiceId, Long attachmentId);
}
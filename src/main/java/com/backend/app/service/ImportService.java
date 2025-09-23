package com.backend.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.ImportResultDTO;

@Service
public interface ImportService {
    
    ImportResultDTO importInvoicesFromCsv(MultipartFile file) throws Exception;
}
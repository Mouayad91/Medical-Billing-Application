package com.backend.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.app.dto.ImportResultDTO;
import com.backend.app.service.ImportService;

@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportService importService;

    /** CSV-Rechnungsimport mit Validierung und Fehlerprotokoll */
    @PostMapping(value = "/invoices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResultDTO> importInvoices(
            @RequestParam("file") MultipartFile file) {
        
        try {
            ImportResultDTO result = importService.importInvoicesFromCsv(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ImportResultDTO errorResult = new ImportResultDTO();
            errorResult.setCreatedCount(0);
            errorResult.setErrors(new java.util.ArrayList<>());
            errorResult.getErrors().add(new com.backend.app.dto.ImportErrorDTO(0, "File processing error: " + e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
    }
}
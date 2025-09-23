package com.backend.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.DunningLogResponseDTO;
import com.backend.app.dto.DunningRunResponseDTO;
import com.backend.app.service.DunningService;

@RestController
@RequestMapping("/api/v1")
public class DunningController {
    
    @Autowired
    private DunningService dunningService;

    /** Mahnlauf für überfällige Rechnungen automatisch durchführen */
    @PostMapping("/dunning/run")
    public ResponseEntity<DunningRunResponseDTO> runDunning(
            @RequestParam(value = "asOf", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOf) {
        
        if (asOf == null) {
            asOf = LocalDate.now();
        }
        
        DunningRunResponseDTO response = dunningService.runDunning(asOf);
        return ResponseEntity.ok(response);
    }

    /** Mahnhistorie mit Eskalationsstufen für Rechnung anzeigen */
    @GetMapping("/invoices/{id}/dunning-logs")
    public ResponseEntity<List<DunningLogResponseDTO>> getDunningLogsByInvoice(@PathVariable Long id) {
        
        List<DunningLogResponseDTO> logs = dunningService.getDunningLogsByInvoiceId(id);
        return ResponseEntity.ok(logs);
    }
}
package com.backend.app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.app.dto.DunningLogResponseDTO;
import com.backend.app.dto.DunningRunResponseDTO;

@Service
public interface DunningService {
    
    DunningRunResponseDTO runDunning(LocalDate asOf);
    
    List<DunningLogResponseDTO> getDunningLogsByInvoiceId(Long invoiceId);
}
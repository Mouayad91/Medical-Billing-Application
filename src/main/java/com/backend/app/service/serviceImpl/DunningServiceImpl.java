package com.backend.app.service.serviceImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.dto.DunningLogResponseDTO;
import com.backend.app.dto.DunningRunResponseDTO;
import com.backend.app.entity.DunningLog;
import com.backend.app.entity.Invoice;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.DunningLogRepository;
import com.backend.app.repository.InvoiceRepository;
import com.backend.app.service.DunningService;

/** Service für Mahnwesen und automatische Mahnstufen */
@Service
public class DunningServiceImpl implements DunningService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private DunningLogRepository dunningLogRepository;
    
    @Override
    @Transactional
    public DunningRunResponseDTO runDunning(LocalDate asOf) {
        // Nur Rechnungen mit offenen Beträgen bearbeiten
        List<Invoice> openInvoices = invoiceRepository.findAll().stream()
            .filter(invoice -> invoice.getStatus() == InvoiceStatus.OPEN || invoice.getStatus() == InvoiceStatus.PARTIALLY_PAID)
            .filter(invoice -> invoice.getOpenCents() > 0)
            .collect(Collectors.toList());
        
        int updatedCount = 0;
        
        for (Invoice invoice : openInvoices) {
            long daysOverdue = ChronoUnit.DAYS.between(invoice.getDueDate(), asOf);
            DunningLevel newLevel = calculateDunningLevel(daysOverdue);
            
            // Nur aktualisieren wenn Mahnstufe geändert, um unnötige DB-Zugriffe zu vermeiden
            if (newLevel != invoice.getDunningLevel()) {
                invoice.setDunningLevel(newLevel);
                invoiceRepository.save(invoice);
                
                // Prüfprotokoll für Mahnstufenerhöhung
                DunningLog log = new DunningLog();
                log.setInvoice(invoice);
                log.setLevel(newLevel);
                log.setNote("Auto-run");
                dunningLogRepository.save(log);
                
                updatedCount++;
            }
        }
        
        return new DunningRunResponseDTO(updatedCount);
    }
    
    @Override
    public List<DunningLogResponseDTO> getDunningLogsByInvoiceId(Long invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new ApiException("Invoice not found with id: " + invoiceId);
        }
        
        List<DunningLog> logs = dunningLogRepository.findByInvoiceIdOrderByLoggedAtDesc(invoiceId);
        return logs.stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }
    
    /** Geschäftsregeln: >45 Tage = INKASSO, >21 Tage = MAHNUNG_2, >7 Tage = MAHNUNG_1 */
    private DunningLevel calculateDunningLevel(long daysOverdue) {
        if (daysOverdue > 45) {
            return DunningLevel.COLLECTION;
        } else if (daysOverdue > 21) {
            return DunningLevel.LEVEL_2;
        } else if (daysOverdue > 7) {
            return DunningLevel.LEVEL_1;
        } else {
            return DunningLevel.NONE;
        }
    }
    
    private DunningLogResponseDTO mapToResponseDTO(DunningLog log) {
        return new DunningLogResponseDTO(
            log.getId(),
            log.getLevel(),
            log.getLoggedAt(),
            log.getNote()
        );
    }
}
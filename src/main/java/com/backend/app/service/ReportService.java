package com.backend.app.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

@Service
public interface ReportService {
    
    String generateInvoicesReport(InvoiceStatus status, DunningLevel dunningLevel, 
                                  LocalDate dateFrom, LocalDate dateTo, String providerName, String debtorName);
}
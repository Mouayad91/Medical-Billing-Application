package com.backend.app.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.app.dto.CreateInvoiceRequestDTO;
import com.backend.app.dto.InvoiceResponseDTO;
import com.backend.app.dto.InvoiceSummaryDTO;
import com.backend.app.dto.UpdateInvoiceRequestDTO;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

@Service
public interface InvoiceService {

    InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO);

    InvoiceResponseDTO getInvoiceById(Long id);
    
    Page<InvoiceSummaryDTO> getAllInvoices(
        InvoiceStatus status,
        DunningLevel dunningLevel,
        LocalDate dateFrom,
        LocalDate dateTo,
        String providerName,
        String debtorName,
        Pageable pageable
    );
    
    InvoiceResponseDTO updateInvoice(Long id, UpdateInvoiceRequestDTO updateRequest);
    
    InvoiceResponseDTO cancelInvoice(Long id);
}

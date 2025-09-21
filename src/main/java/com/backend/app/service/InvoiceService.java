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
    
    /**
     * Get all invoices with filtering and pagination
     * 
     * @param status Filter by invoice status 
     * @param dunningLevel Filter by dunning level 
     * @param dateFrom Filter by invoice date from 
     * @param dateTo Filter by invoice date to
     * @param providerName Filter by provider name
     * @param debtorName Filter by debtor name
     * @param pageable Pagination and sorting
     * @return Page of InvoiceSummaryDTO
     */
    Page<InvoiceSummaryDTO> getAllInvoices(
        InvoiceStatus status,
        DunningLevel dunningLevel,
        LocalDate dateFrom,
        LocalDate dateTo,
        String providerName,
        String debtorName,
        Pageable pageable
    );
    
    /**
     * Update invoice (e.g. due date correction)
     * 
     * @param id Invoice ID
     * @param updateRequest Update data
     * @return Updated invoice
     * @throws ApiException if invoice cannot be updated (409 if already dunned)
     */
    InvoiceResponseDTO updateInvoice(Long id, UpdateInvoiceRequestDTO updateRequest);
    
    /**
     * Cancel invoice (storno)
     * 
     * @param id Invoice ID
     * @return Cancelled invoice
     * @throws ApiException if invoice cannot be cancelled (409 if paid)
     */
    InvoiceResponseDTO cancelInvoice(Long id);
}

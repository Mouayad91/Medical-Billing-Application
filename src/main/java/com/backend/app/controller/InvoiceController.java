package com.backend.app.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.CreateInvoiceRequestDTO;
import com.backend.app.dto.InvoiceResponseDTO;
import com.backend.app.dto.InvoiceSummaryDTO;
import com.backend.app.dto.UpdateInvoiceRequestDTO;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.service.InvoiceService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/v1")
public class InvoiceController {
    
    @Autowired
    private InvoiceService invoiceService;

    /**
     * GET /invoices
     * 
     * Zweck: Worklists (z. B. offene/past-due Rechnungen), Reporting-Filter.
     * Rollen: BILLING, CONTROLLER, COLLECTIONS, ADMIN
     * 
     * Query Parameter:
     * - status: OPEN|PARTIALLY_PAID|PAID|CANCELLED
     * - dunning: NONE|LEVEL_1|LEVEL_2|COLLECTION
     * - from/to: YYYY-MM-DD (nach invoiceDate)
     * - provider/debtor: Namenssuche
     * - page,size,sort: Pagination
     */
    @GetMapping("/invoices")
    public ResponseEntity<Page<InvoiceSummaryDTO>> getAllInvoices(
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) DunningLevel dunning,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String debtor,
            @PageableDefault(size = 20, sort = "dueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        
        Page<InvoiceSummaryDTO> invoices = invoiceService.getAllInvoices(
            status, dunning, from, to, provider, debtor, pageable
        );
        
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO createInvoiceRequestDTO){

        InvoiceResponseDTO savedInvoice = invoiceService.createInvoice(createInvoiceRequestDTO);

        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }
    

    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDTO invoice = invoiceService.getInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
    
    /**
     * PATCH /invoices/{id}
     * 
     * Zweck: z. B. Fälligkeitsdatum korrigieren (vor Dunning).
     * Rollen: BILLING, ADMIN
     * 
     * Geschäftsregel: DueDate-Änderung nach Mahnlauf blocken (Datenintegrität).
     */
    @PutMapping("/invoices/{id}")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateInvoiceRequestDTO updateRequest) {
        
        InvoiceResponseDTO updatedInvoice = invoiceService.updateInvoice(id, updateRequest);
        
        return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
    }
    
    /**
     * POST /invoices/{id}/cancel
     * 
     * Zweck: Storno nur solange unbezahlt.
     * Rollen: BILLING, ADMIN
     * 
     * Geschäftsregel: paidCents>0 → nicht erlaubt (409)
     */
    @PostMapping("/invoices/{id}/cancel")
    public ResponseEntity<InvoiceResponseDTO> cancelInvoice(@PathVariable Long id) {
        
        InvoiceResponseDTO cancelledInvoice = invoiceService.cancelInvoice(id);
        
        return new ResponseEntity<>(cancelledInvoice, HttpStatus.OK);
    }
}

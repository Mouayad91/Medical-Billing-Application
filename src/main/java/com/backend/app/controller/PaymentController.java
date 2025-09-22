package com.backend.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.AddPaymentRequestDTO;
import com.backend.app.dto.PaymentResponseDTO;
import com.backend.app.dto.PaymentSummaryDTO;
import com.backend.app.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    /**
     * POST /invoices/{id}/payments
     * Zahlung zu einer Rechnung hinzufügen
     * Rollen: COLLECTIONS, BILLING, ADMIN (TODO: Security implementation needed)
     */
    @PostMapping("/invoices/{id}/payments")
    public ResponseEntity<PaymentResponseDTO> addPaymentToInvoice(
            @PathVariable Long id,
            @Valid @RequestBody AddPaymentRequestDTO request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        
        PaymentResponseDTO response = paymentService.addPaymentToInvoice(id, request, idempotencyKey);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /invoices/{id}/payments
     * Zahlungshistorie einer Rechnung anzeigen
     * Rollen: COLLECTIONS, BILLING, CONTROLLER, ADMIN (TODO: Security implementation needed)
     */
    @GetMapping("/invoices/{id}/payments")
    public ResponseEntity<List<PaymentSummaryDTO>> getPaymentsByInvoice(@PathVariable Long id) {
        
        List<PaymentSummaryDTO> payments = paymentService.getPaymentsByInvoiceId(id);
        return ResponseEntity.ok(payments);
    }
}

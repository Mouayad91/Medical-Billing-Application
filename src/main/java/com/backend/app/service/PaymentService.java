package com.backend.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.app.dto.AddPaymentRequestDTO;
import com.backend.app.dto.PaymentResponseDTO;
import com.backend.app.dto.PaymentSummaryDTO;

@Service
public interface PaymentService {
    
    /**
     * Zahlung zu einer Rechnung hinzufügen mit Idempotency-Schutz
     * @param invoiceId Invoice ID
     * @param request Payment request data
     * @param idempotencyKey Optional idempotency key for duplicate prevention
     * @return PaymentResponseDTO
     */
    PaymentResponseDTO addPaymentToInvoice(Long invoiceId, AddPaymentRequestDTO request, String idempotencyKey);
    
    /**
     * Alle Zahlungen einer Rechnung abrufen
     * @param invoiceId Invoice ID
     * @return List of PaymentSummaryDTO
     */
    List<PaymentSummaryDTO> getPaymentsByInvoiceId(Long invoiceId);
}

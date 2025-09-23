package com.backend.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.app.dto.AddPaymentRequestDTO;
import com.backend.app.dto.PaymentResponseDTO;
import com.backend.app.dto.PaymentSummaryDTO;

@Service
public interface PaymentService {
    
    PaymentResponseDTO addPaymentToInvoice(Long invoiceId, AddPaymentRequestDTO request, String idempotencyKey);
    
    List<PaymentSummaryDTO> getPaymentsByInvoiceId(Long invoiceId);
}

package com.backend.app.service.serviceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.app.dto.AddPaymentRequestDTO;
import com.backend.app.dto.PaymentResponseDTO;
import com.backend.app.dto.PaymentSummaryDTO;
import com.backend.app.entity.Invoice;
import com.backend.app.entity.Payment;
import com.backend.app.entity.PaymentIdempotency;
import com.backend.app.enums.InvoiceStatus;
import com.backend.app.exception.ApiException;
import com.backend.app.exception.ConflictException;
import com.backend.app.repository.InvoiceRepository;
import com.backend.app.repository.PaymentIdempotencyRepository;
import com.backend.app.repository.PaymentRepository;
import com.backend.app.service.PaymentService;

/** Service für Zahlungseingänge und Rechnungsausgleich */
@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private PaymentIdempotencyRepository paymentIdempotencyRepository;
    
    @Override
    @Transactional
    public PaymentResponseDTO addPaymentToInvoice(Long invoiceId, AddPaymentRequestDTO request, String idempotencyKey) {
        if (request.getAmountCents() == null || request.getAmountCents() <= 0) {
            throw new ApiException("amountCents must be greater than 0");
        }
        
        // Doppelzahlungen mit Idempotenz-Schlüssel verhindern
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            Optional<PaymentIdempotency> existing = paymentIdempotencyRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                Payment existingPayment = paymentRepository.findById(existing.get().getPaymentId())
                    .orElseThrow(() -> new ApiException("Idempotency conflict: payment not found"));
                return mapToResponseDTO(existingPayment);
            }
        }
        
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new ApiException("Invoice not found with id: " + invoiceId));
            
        // Geschäftsregel: stornierte Rechnungen können nicht bezahlt werden
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ApiException("Cannot add payment to cancelled invoice");
        }
        
        // Geschäftsregel: Überzahlungen verhindern
        int openCents = invoice.getOpenCents();
        if (request.getAmountCents() > openCents) {
            throw new ConflictException("Payment amount (" + request.getAmountCents() + " cents) exceeds open amount (" + openCents + " cents)");
        }
        
        // Zahlung erstellen
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmountCents(request.getAmountCents());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setMethod(request.getMethod());
        payment.setNote(request.getNote());
        
        payment = paymentRepository.save(payment);
        
        // Rechnungssaldo und Status aktualisieren
        updateInvoiceAfterPayment(invoice, request.getAmountCents());
        
        // Idempotenz-Schlüssel speichern zur Duplikatsprävention (läuft nach 24h ab)
        if (idempotencyKey != null && !idempotencyKey.trim().isEmpty()) {
            PaymentIdempotency idempotencyRecord = new PaymentIdempotency();
            idempotencyRecord.setIdempotencyKey(idempotencyKey);
            idempotencyRecord.setInvoiceId(invoiceId);
            idempotencyRecord.setPaymentId(payment.getId());
            idempotencyRecord.setCreatedAt(Instant.now());
            idempotencyRecord.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
            paymentIdempotencyRepository.save(idempotencyRecord);
        }
        
        return mapToResponseDTO(payment);
    }
    
    @Override
    public List<PaymentSummaryDTO> getPaymentsByInvoiceId(Long invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new ApiException("Invoice not found with id: " + invoiceId);
        }
        
        List<Payment> payments = paymentRepository.findByInvoiceIdOrderByPaymentDateDesc(invoiceId);
        return payments.stream()
            .map(this::mapToSummaryDTO)
            .collect(Collectors.toList());
    }
    
    /** Rechnungssaldo und Status nach Zahlungseingang aktualisieren */
    private void updateInvoiceAfterPayment(Invoice invoice, int paymentAmount) {
        invoice.setPaidCents(invoice.getPaidCents() + paymentAmount);
        
        // Status basierend auf Restsaldo aktualisieren
        int newOpenCents = invoice.getOpenCents();
        if (newOpenCents == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (invoice.getPaidCents() > 0 && invoice.getStatus() == InvoiceStatus.OPEN) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        
        invoiceRepository.save(invoice);
    }
    
    private PaymentResponseDTO mapToResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
            payment.getId(),
            payment.getAmountCents(),
            payment.getMethod(),
            payment.getPaymentDate(),
            payment.getNote()
        );
    }
    
    private PaymentSummaryDTO mapToSummaryDTO(Payment payment) {
        PaymentSummaryDTO dto = new PaymentSummaryDTO();
        dto.setId(payment.getId());
        dto.setAmountCents(payment.getAmountCents());
        dto.setMethod(payment.getMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setNote(payment.getNote());
        return dto;
    }
}

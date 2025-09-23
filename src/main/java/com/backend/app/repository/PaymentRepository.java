package com.backend.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Payment;

/** Repository für Zahlungsverwaltung und Zahlungshistorie */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /** Zahlungshistorie zur Rechnung laden, neueste zuerst */
    @Query("SELECT p FROM Payment p WHERE p.invoice.id = :invoiceId ORDER BY p.paymentDate DESC, p.id DESC")
    List<Payment> findByInvoiceIdOrderByPaymentDateDesc(@Param("invoiceId") Long invoiceId);
    
    List<Payment> findByInvoiceId(Long invoiceId);
}

package com.backend.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Find all payments for a specific invoice, ordered by payment date descending
     */
    @Query("SELECT p FROM Payment p WHERE p.invoice.id = :invoiceId ORDER BY p.paymentDate DESC, p.id DESC")
    List<Payment> findByInvoiceIdOrderByPaymentDateDesc(@Param("invoiceId") Long invoiceId);
    
    /**
     * Find all payments for a specific invoice
     */
    List<Payment> findByInvoiceId(Long invoiceId);
}

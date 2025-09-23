package com.backend.app.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Prevents duplicate payment processing using idempotency keys */
@Entity
@Table(name = "payment_idempotency", 
       indexes = {@Index(name="ix_payment_idempotency_key", columnList="idempotencyKey", unique = true)})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIdempotency {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(unique = true, length = 100)
    private String idempotencyKey;
    
    @NotNull
    private Long invoiceId;
    
    @NotNull
    private Long paymentId;
    
    @NotNull
    private Instant createdAt = Instant.now();
    
    // TTL für Cleanup nach z.B. 24h oder 7 Tagen
    private Instant expiresAt;
}
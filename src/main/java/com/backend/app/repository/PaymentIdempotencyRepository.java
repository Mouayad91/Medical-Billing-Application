package com.backend.app.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.PaymentIdempotency;

@Repository
public interface PaymentIdempotencyRepository extends JpaRepository<PaymentIdempotency, Long> {
    
    Optional<PaymentIdempotency> findByIdempotencyKey(String idempotencyKey);
    
    void deleteByExpiresAtBefore(Instant expiredBefore);
}
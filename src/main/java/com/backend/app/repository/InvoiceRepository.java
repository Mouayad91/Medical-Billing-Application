package com.backend.app.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Invoice;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

/**
 * Repository für Rechnungsverwaltung und Rechnungssuche
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Simple query methods following Spring Data JPA naming conventions
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

    Page<Invoice> findByDunningLevel(DunningLevel dunningLevel, Pageable pageable);

    Page<Invoice> findByStatusAndDunningLevel(InvoiceStatus status, DunningLevel dunningLevel, Pageable pageable);

    Page<Invoice> findByInvoiceDateBetween(LocalDate dateFrom, LocalDate dateTo, Pageable pageable);

    Page<Invoice> findByStatusAndInvoiceDateBetween(InvoiceStatus status, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);

    Page<Invoice> findByDunningLevelAndInvoiceDateBetween(DunningLevel dunningLevel, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);

    Page<Invoice> findByStatusAndDunningLevelAndInvoiceDateBetween(InvoiceStatus status, DunningLevel dunningLevel, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);
}

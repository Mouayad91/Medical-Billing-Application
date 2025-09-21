package com.backend.app.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Invoice;
import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    /**
     * Find invoices with basic filters 
     */
    @Query("SELECT i FROM Invoice i " +
           "WHERE (:status IS NULL OR i.status = :status) " +
           "AND (:dunningLevel IS NULL OR i.dunningLevel = :dunningLevel) " +
           "AND (:dateFrom IS NULL OR i.invoiceDate >= :dateFrom) " +
           "AND (:dateTo IS NULL OR i.invoiceDate <= :dateTo)")
    Page<Invoice> findInvoicesWithFilters(
        @Param("status") InvoiceStatus status,
        @Param("dunningLevel") DunningLevel dunningLevel,
        @Param("dateFrom") LocalDate dateFrom,
        @Param("dateTo") LocalDate dateTo,
        @Param("providerName") String providerName,
        @Param("debtorName") String debtorName,
        Pageable pageable
    );
}

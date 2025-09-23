package com.backend.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.DunningLog;

/** Repository für Mahnungshistorie und Mahnlauf-Protokolle */
@Repository
public interface DunningLogRepository extends JpaRepository<DunningLog, Long> {
    
    @Query("SELECT d FROM DunningLog d WHERE d.invoice.id = :invoiceId ORDER BY d.loggedAt DESC")
    List<DunningLog> findByInvoiceIdOrderByLoggedAtDesc(@Param("invoiceId") Long invoiceId);
}
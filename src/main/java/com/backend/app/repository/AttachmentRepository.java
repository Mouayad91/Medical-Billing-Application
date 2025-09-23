package com.backend.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Attachment;

/** Repository für Rechnungsanhänge und Dokumente */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    /** Anhänge zur Rechnung laden, neueste zuerst */
    @Query("SELECT a FROM Attachment a WHERE a.invoice.id = :invoiceId ORDER BY a.uploadedAt DESC")
    List<Attachment> findByInvoiceIdOrderByUploadedAtDesc(@Param("invoiceId") Long invoiceId);
    
    /** Sicherheitsprüfung: Anhang muss zur angegebenen Rechnung gehören */
    @Query("SELECT a FROM Attachment a WHERE a.id = :attachmentId AND a.invoice.id = :invoiceId")
    Optional<Attachment> findByIdAndInvoiceId(@Param("attachmentId") Long attachmentId, @Param("invoiceId") Long invoiceId);
}
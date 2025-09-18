package com.backend.app.entity;


import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attachment",
        indexes = {@Index(name="ix_attachment_invoice", columnList="invoice_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name="invoice_id")
    @NotNull private Invoice invoice;

    @NotBlank private String filename;
    private String contentType;

    // lokaler Pfad; in echt z. B. S3-URL/Key
    @NotBlank private String storagePath;

    @CreationTimestamp
    private Instant uploadedAt;
}
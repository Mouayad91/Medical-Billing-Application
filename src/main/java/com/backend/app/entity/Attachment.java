package com.backend.app.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

    // Für Demo: lokaler Pfad; in echt z. B. S3-URL/Key
    @NotBlank private String storagePath;

    @CreationTimestamp
    private Instant uploadedAt;
}
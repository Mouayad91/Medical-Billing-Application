package com.backend.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "service_catalog",
        indexes = {@Index(name="ix_service_code", columnList="code", unique = true)})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ServiceCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code cant be blank")
    private String code;        // z. B. GOÄ-Nr.
    @NotBlank (message = "Description cant be blank")
    private String description;

    @Min(value = 0, message = "Base fee must be non-negative")
    private int baseFeeCents;             // Grundpreis in Cent

    @CreationTimestamp
    private Instant createdAt;
}

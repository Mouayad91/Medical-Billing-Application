package com.backend.app.entity;


import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice",
        indexes = {
                @Index(name="ix_invoice_provider", columnList="provider_id"),
                @Index(name="ix_invoice_patient", columnList="patient_id"),
                @Index(name="ix_invoice_status", columnList="status"),
                @Index(name="ix_invoice_due", columnList="dueDate")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name="provider_id")
    @NotNull private DoctorClient provider;

    @ManyToOne(optional = false) @JoinColumn(name="patient_id")
    @NotNull private Patient patient;

    @NotNull private LocalDate invoiceDate;
    @NotNull private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.OPEN;

    @Builder.Default private int totalCents = 0;
    @Builder.Default private int paidCents = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DunningLevel dunningLevel = DunningLevel.NONE;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @ToString.Exclude @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @Builder.Default
    private List<DunningLog> dunningLogs = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @ToString.Exclude @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    @Transient
    public int getOpenCents() { return Math.max(totalCents - paidCents, 0); }
}
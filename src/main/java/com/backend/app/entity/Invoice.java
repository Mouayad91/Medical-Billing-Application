package com.backend.app.entity;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.backend.app.enums.DunningLevel;
import com.backend.app.enums.InvoiceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private InvoiceStatus status = InvoiceStatus.OPEN;

    private int totalCents = 0;
   private int paidCents = 0;

    @Enumerated(EnumType.STRING)
    private DunningLevel dunningLevel = DunningLevel.NONE;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @ToString.Exclude 
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude 
    private List<DunningLog> dunningLogs = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Attachment> attachments = new ArrayList<>();

    @Transient
    public int getOpenCents() { return Math.max(totalCents - paidCents, 0); }
}
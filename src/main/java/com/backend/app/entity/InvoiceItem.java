package com.backend.app.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_item",
        indexes = {@Index(name="ix_item_invoice", columnList="invoice_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="invoice_id")
    @NotNull private Invoice invoice;

    @ManyToOne(optional = false)
    @JoinColumn(name="service_id")
    @NotNull private ServiceCatalog service;

    @Min(1) private int quantity;

    // GOÄ-ähnlicher Steigerungsfaktor
    @Column(precision = 6, scale = 2)
    @NotNull private BigDecimal factor; // z. B. 2.30

    @Min(0) private int unitPriceCents;   // Snapshot aus ServiceCatalog
    @Min(0) private int surchargeCents;   // Nacht/Notfal
    @Min(0) private int lineTotalCents;   // vom Service berechnet
}

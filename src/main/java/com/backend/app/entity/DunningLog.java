package com.backend.app.entity;


import com.backend.app.enums.DunningLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity @Table(name = "dunning_log",
        indexes = {@Index(name="ix_dunning_invoice", columnList="invoice_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DunningLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name="invoice_id")
    @NotNull private Invoice invoice;

    @NotNull @Enumerated(EnumType.STRING)
    private DunningLevel level;

    @CreationTimestamp
    private Instant loggedAt;

    private String note;
}
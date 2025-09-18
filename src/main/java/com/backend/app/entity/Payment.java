package com.backend.app.entity;
import com.backend.app.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment",
        indexes = {@Index(name="ix_payment_invoice", columnList="invoice_id")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name="invoice_id")
    @NotNull private Invoice invoice;

    @Min(1) private int amountCents;

    @NotNull private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String note;
}
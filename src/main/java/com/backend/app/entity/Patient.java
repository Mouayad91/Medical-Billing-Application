package com.backend.app.entity;




import com.backend.app.enums.PayerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient",
        indexes = {@Index(name="ix_patient_name", columnList="lastName,firstName")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String firstName;
    @NotBlank private String lastName;

    private LocalDate dateOfBirth;

    @Email private String email;
    private String address;

    @Enumerated(EnumType.STRING)
    private PayerType payerType = PayerType.SELF_PAY;

    private String payerDetails; // Versicherungsnummer, Beihilfenummer etc.

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "patient")
    @ToString.Exclude
    private List<Invoice> invoices = new ArrayList<>();
}

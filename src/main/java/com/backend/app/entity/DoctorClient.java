package com.backend.app.entity;


import com.backend.app.enums.ProviderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor_client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorClient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank(message = "Name is cant be blank")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProviderType type;

    private String specialty;
    private String taxId;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "provider")
    @ToString.Exclude

    private List<Invoice> invoices = new ArrayList<>();
}

package com.backend.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Debtor;

/** Repository für Rechnungsempfänger (Patienten) */
@Repository
public interface DebtorRepository extends JpaRepository<Debtor, Long> {
    
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Debtor findByFirstNameAndLastName(String firstName, String lastName);
    
    Page<Debtor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);
}
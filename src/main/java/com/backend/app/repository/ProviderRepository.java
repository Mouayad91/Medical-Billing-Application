package com.backend.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Provider;

/** Repository für Abrechnungspartner (Ärzte, Praxen, Kliniken) */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    
    boolean existsByName(String name);

    Provider findByName(String name);
    
    Page<Provider> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}

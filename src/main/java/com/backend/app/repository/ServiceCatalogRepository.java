package com.backend.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.app.entity.ServiceCatalog;

@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    
    ServiceCatalog findByCode(String code);
    
    Page<ServiceCatalog> findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String code, String description, Pageable pageable);
}

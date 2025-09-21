package com.backend.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.app.entity.Provider;




@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    

    boolean existsByName(String name);

    Provider findByName(String name);
}

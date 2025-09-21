package com.backend.app.service;

import org.springframework.stereotype.Service;
import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.dto.CreateProviderRequestDTO;
import com.backend.app.dto.ProviderPageResponseDTO;


@Service
public interface ProviderService {

    ProviderResponseDTO createProvider(CreateProviderRequestDTO createProviderRequestDTO);
    
    ProviderPageResponseDTO getAllProviders(int pageNo, int pageSize, String sortBy, String sortDir);
    
    ProviderPageResponseDTO getProvidersByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);
    
    ProviderResponseDTO getProviderById(Long id);
}

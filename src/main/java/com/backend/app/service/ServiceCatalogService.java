package com.backend.app.service;

import org.springframework.stereotype.Service;

import com.backend.app.dto.ServiceResponseDTO;
import com.backend.app.dto.CreateServiceRequestDTO;
import com.backend.app.dto.ServicePageResponseDTO;


@Service
public interface ServiceCatalogService {
    
    ServiceResponseDTO createService(CreateServiceRequestDTO createServiceRequestDTO);
    
    ServicePageResponseDTO getAllServices(int pageNo, int pageSize, String sortBy, String sortDir);
    
    ServicePageResponseDTO getServicesByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);
    
    ServiceResponseDTO getServiceById(Long id);
}

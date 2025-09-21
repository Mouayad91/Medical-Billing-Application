package com.backend.app.service;

import org.springframework.stereotype.Service;
import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.dto.CreateProviderRequestDTO;


@Service
public interface ProviderService {


    ProviderResponseDTO createProvider(CreateProviderRequestDTO createProviderRequestDTO);

}

package com.backend.app.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.app.dto.CreateProviderRequestDTO;
import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.entity.Provider;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.ProviderRepository;
import com.backend.app.service.ProviderService;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProviderResponseDTO createProvider(CreateProviderRequestDTO createProviderRequestDTO) {

        Provider providerFromDB = providerRepository.findByName(createProviderRequestDTO.getName());

        if (providerFromDB != null) {
            throw new ApiException("Provider with name " + createProviderRequestDTO.getName() + " already exists");
        }

        Provider provider = new Provider();
        provider.setName(createProviderRequestDTO.getName());
        provider.setType(createProviderRequestDTO.getType());
        provider.setSpecialty(createProviderRequestDTO.getSpecialty());
        provider.setTaxId(createProviderRequestDTO.getTaxId());

        Provider saved = providerRepository.save(provider);

        return modelMapper.map(saved, ProviderResponseDTO.class);
    }
}
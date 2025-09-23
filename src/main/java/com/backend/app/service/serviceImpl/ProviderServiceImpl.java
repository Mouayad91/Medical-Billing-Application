package com.backend.app.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.app.dto.CreateProviderRequestDTO;
import com.backend.app.dto.ProviderPageResponseDTO;
import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.entity.Provider;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.ProviderRepository;
import com.backend.app.service.ProviderService;

/** Service für Abrechnungspartner-Verwaltung (Ärzte, Praxen, Kliniken) */
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


    @Override
    public ProviderPageResponseDTO getAllProviders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);
        
        Page<Provider> providersPage = providerRepository.findAll(pageDetails);
        
        if (providersPage.isEmpty()) {
            throw new ApiException("No providers found");
        }

        List<ProviderResponseDTO> providerResponseDTOs = providersPage.getContent().stream()
                .map(provider -> modelMapper.map(provider, ProviderResponseDTO.class))
                .collect(Collectors.toList());

        ProviderPageResponseDTO response = new ProviderPageResponseDTO();
        response.setContent(providerResponseDTOs);
        response.setPageNo(providersPage.getNumber());
        response.setPageSize(providersPage.getSize());
        response.setTotalElements(providersPage.getTotalElements());
        response.setTotalPages(providersPage.getTotalPages());
        response.setLast(providersPage.isLast());
        response.setFirst(providersPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public ProviderPageResponseDTO getProvidersByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);

        Page<Provider> providersPage = providerRepository.findByNameContainingIgnoreCase(keyword, pageDetails);

        if (providersPage.isEmpty()) {
            throw new ApiException("No providers found with keyword: " + keyword);
        }

        List<ProviderResponseDTO> providerResponseDTOs = providersPage.getContent().stream()
                .map(provider -> modelMapper.map(provider, ProviderResponseDTO.class))
                .collect(Collectors.toList());
        
        ProviderPageResponseDTO response = new ProviderPageResponseDTO();
        response.setContent(providerResponseDTOs);
        response.setPageNo(providersPage.getNumber());
        response.setPageSize(providersPage.getSize());
        response.setTotalElements(providersPage.getTotalElements());
        response.setTotalPages(providersPage.getTotalPages());
        response.setLast(providersPage.isLast());
        response.setFirst(providersPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public ProviderResponseDTO getProviderById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ApiException("Provider with id " + id + " not found"));
        
        return modelMapper.map(provider, ProviderResponseDTO.class);
    }


}
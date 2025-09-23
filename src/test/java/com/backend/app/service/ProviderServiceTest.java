package com.backend.app.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.entity.Provider;
import com.backend.app.enums.ProviderType;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.ProviderRepository;
import com.backend.app.service.serviceImpl.ProviderServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private Provider testProvider;
    private ProviderResponseDTO testProviderResponseDTO;

    @BeforeEach
    void setUp() {
        // Test-Provider erstellen
        testProvider = new Provider();
        testProvider.setId(1L);
        testProvider.setName("Dr. Müller Praxis");
        testProvider.setType(ProviderType.PRAXIS);
        testProvider.setSpecialty("Allgemeinmedizin");
        testProvider.setTaxId("DE123456789");

        // Test-ProviderResponseDTO erstellen
        testProviderResponseDTO = new ProviderResponseDTO();
        testProviderResponseDTO.setId(1L);
        testProviderResponseDTO.setName("Dr. Müller Praxis");
        testProviderResponseDTO.setType(ProviderType.PRAXIS);
        testProviderResponseDTO.setSpecialty("Allgemeinmedizin");
        testProviderResponseDTO.setTaxId("DE123456789");
    }

    @Test
    void getProviderById_ShouldReturnProvider_WhenProviderExists() {
        
        Long providerId = 1L;
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(testProvider));
        when(modelMapper.map(testProvider, ProviderResponseDTO.class)).thenReturn(testProviderResponseDTO);

     
        ProviderResponseDTO result = providerService.getProviderById(providerId);

       
        assertNotNull(result);
        assertEquals(testProviderResponseDTO.getId(), result.getId());
        assertEquals(testProviderResponseDTO.getName(), result.getName());
        assertEquals(testProviderResponseDTO.getType(), result.getType());
        assertEquals(testProviderResponseDTO.getSpecialty(), result.getSpecialty());
        assertEquals(testProviderResponseDTO.getTaxId(), result.getTaxId());

       
        verify(providerRepository, times(1)).findById(providerId);
        verify(modelMapper, times(1)).map(testProvider, ProviderResponseDTO.class);
    }

    @Test
    void getProviderById_ShouldThrowApiException_WhenProviderNotExists() {
       
        Long nonExistentProviderId = 999L;
        when(providerRepository.findById(nonExistentProviderId)).thenReturn(Optional.empty());

     
        ApiException exception = assertThrows(ApiException.class, 
            () -> providerService.getProviderById(nonExistentProviderId));

        assertEquals("Provider with id " + nonExistentProviderId + " not found", exception.getMessage());
        
        
        verify(providerRepository, times(1)).findById(nonExistentProviderId);
        // ModelMapper should not be called when provider not found
        verify(modelMapper, never()).map(any(), any());
    }
}
package com.backend.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.CreateProviderRequestDTO;
import com.backend.app.dto.ProviderResponseDTO;
import com.backend.app.dto.ProviderPageResponseDTO;
import com.backend.app.service.ProviderService;
import com.backend.app.config.AppConstants;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class ProviderController {

    @Autowired
    private ProviderService providerService;



    /** Neuen Abrechnungspartner (Arzt/Praxis/Klinik) anlegen */
    @PostMapping("/providers")
    public ResponseEntity<ProviderResponseDTO> createProvider(@RequestBody CreateProviderRequestDTO createProviderRequestDTO){

        ProviderResponseDTO savedProvider = providerService.createProvider(createProviderRequestDTO);

        return new ResponseEntity<>(savedProvider, HttpStatus.CREATED);

    }

    /** Alle Abrechnungspartner mit Pagination und Sortierung */
    @GetMapping("/providers")
    public ResponseEntity<ProviderPageResponseDTO> getAllProviders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        ProviderPageResponseDTO providers = providerService.getAllProviders(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(providers, HttpStatus.OK);
    }

    /** Einzelnen Abrechnungspartner nach ID abrufen */
    @GetMapping("/providers/{id}")
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable Long id) {
        ProviderResponseDTO provider = providerService.getProviderById(id);
        return new ResponseEntity<>(provider, HttpStatus.OK);
    }

    /** Abrechnungspartner nach Namen suchen für Rechnungserstellung */
    @GetMapping(value = "/providers", params = "name")
    public ResponseEntity<ProviderPageResponseDTO> getProvidersByKeyword(
            @RequestParam("name") String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        ProviderPageResponseDTO providersResponse = providerService.getProvidersByKeyword(keyword, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(providersResponse, HttpStatus.OK);
    }

}
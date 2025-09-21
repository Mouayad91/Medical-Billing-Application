package com.backend.app.controller;

import java.util.List;
import java.util.stream.Stream;

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
import com.backend.app.exception.ApiException;
import com.backend.app.service.ProviderService;




@RestController
@RequestMapping("/api/v1")
public class ProviderController {

    @Autowired
    private ProviderService providerService;



    /* Provider erstellen 
     * POST /providers
     * Zweck: Abrechnungspartner (Ärzte/Praxen/Kliniken) anlegen - ohne Provider keine Rechnung möglich
     * Verwendet von: ROLE_BILLING,ROLE_ADMIN
    */


    @PostMapping("/providers")
    public ResponseEntity<ProviderResponseDTO> createProvider(@RequestBody CreateProviderRequestDTO createProviderRequestDTO){

        ProviderResponseDTO savedProvider = providerService.createProvider(createProviderRequestDTO);

        return new ResponseEntity<>(savedProvider, HttpStatus.CREATED);

    }

    @GetMapping("/providers")
   public ResponseEntity<List<ProviderResponseDTO>> getAllProviders() {
        List<ProviderResponseDTO> providers = providerService.getAllProviders(); // get all ProviderResponseDTOs from service

    return new ResponseEntity<>(providers, HttpStatus.OK);
    }
}
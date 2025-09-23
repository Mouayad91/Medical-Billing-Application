package com.backend.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.backend.app.dto.ServiceResponseDTO;
import com.backend.app.dto.CreateServiceRequestDTO;
import com.backend.app.dto.ServicePageResponseDTO;
import com.backend.app.service.ServiceCatalogService;
import com.backend.app.config.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/v1")
public class ServiceCatalogController {

    @Autowired
    private ServiceCatalogService serviceCatalogService;




    /** Neue abrechenbare Leistung im Katalog anlegen */
    @PostMapping("/services")
    public ResponseEntity<ServiceResponseDTO> createService(@RequestBody CreateServiceRequestDTO createServiceRequestDTO){

        ServiceResponseDTO savedService = serviceCatalogService.createService(createServiceRequestDTO);

        return new ResponseEntity<>(savedService, HttpStatus.CREATED);

    }

    /** Leistungskatalog mit Pagination für Rechnungserstellung */
    @GetMapping("/services")
    public ResponseEntity<ServicePageResponseDTO> getAllServices(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "code", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        ServicePageResponseDTO services = serviceCatalogService.getAllServices(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    /** Einzelne Leistung nach ID für Rechnungsposition abrufen */
    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceResponseDTO> getServiceById(@PathVariable Long id) {
        ServiceResponseDTO service = serviceCatalogService.getServiceById(id);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    /** Leistungen nach Code oder Beschreibung durchsuchen */
    @GetMapping(value = "/services", params = "code")
    public ResponseEntity<ServicePageResponseDTO> getServicesByKeyword(
            @RequestParam("code") String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "code", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        ServicePageResponseDTO servicesResponse = serviceCatalogService.getServicesByKeyword(keyword, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(servicesResponse, HttpStatus.OK);
    }


}

package com.backend.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.app.dto.CreateDebtorRequestDTO;
import com.backend.app.dto.DebtorResponseDTO;
import com.backend.app.dto.DebtorPageResponseDTO;
import com.backend.app.service.DebtorService;
import com.backend.app.config.AppConstants;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1")
public class DebtorController {

    @Autowired
    private DebtorService debtorService;



    /* Schuldner erstellen 
     * POST /debtors
     * Zweck: Rechnungsempfänger (Patient/Versicherung/Beihilfe) anlegen - ohne Debtor keine Rechnung möglich
     * Verwendet von: ROLE_BILLING,ROLE_ADMIN
    */


    @PostMapping("/debtors")
    public ResponseEntity<DebtorResponseDTO> createDebtor(@RequestBody CreateDebtorRequestDTO createDebtorRequestDTO){

        DebtorResponseDTO savedDebtor = debtorService.createDebtor(createDebtorRequestDTO);

        return new ResponseEntity<>(savedDebtor, HttpStatus.CREATED);

    }

    /**
     * GET /debtors
     * Zweck: Alle Schuldner mit Paginierung und Sortierung auflisten
     * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
     */
    @GetMapping("/debtors")
    public ResponseEntity<DebtorPageResponseDTO> getAllDebtors(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "lastName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        DebtorPageResponseDTO debtors = debtorService.getAllDebtors(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(debtors, HttpStatus.OK);
    }

    /**
     * GET /debtors/{id}
     * Zweck: Einzelner Schuldner nach ID
     * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
     */
    @GetMapping("/debtors/{id}")
    public ResponseEntity<DebtorResponseDTO> getDebtorById(@PathVariable Long id) {
        DebtorResponseDTO debtor = debtorService.getDebtorById(id);
        return new ResponseEntity<>(debtor, HttpStatus.OK);
    }

    /**
     * GET /debtors?name=...
     * Zweck: Schuldner nach Name suchen mit Paginierung und Sortierung
     * Verwendet von: ROLE_BILLING, ROLE_CONTROLLER, ROLE_COLLECTIONS, ROLE_ADMIN
     */
    @GetMapping(value = "/debtors", params = "name")
    public ResponseEntity<DebtorPageResponseDTO> getDebtorsByKeyword(
            @RequestParam("name") String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "lastName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortDir) {
        
        DebtorPageResponseDTO debtorsResponse = debtorService.getDebtorsByKeyword(keyword, pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(debtorsResponse, HttpStatus.OK);
    }

}
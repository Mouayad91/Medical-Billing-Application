package com.backend.app.service;

import org.springframework.stereotype.Service;
import com.backend.app.dto.DebtorResponseDTO;
import com.backend.app.dto.CreateDebtorRequestDTO;
import com.backend.app.dto.DebtorPageResponseDTO;


@Service
public interface DebtorService {

    DebtorResponseDTO createDebtor(CreateDebtorRequestDTO createDebtorRequestDTO);
    
    DebtorPageResponseDTO getAllDebtors(int pageNo, int pageSize, String sortBy, String sortDir);
    
    DebtorPageResponseDTO getDebtorsByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);
    
    DebtorResponseDTO getDebtorById(Long id);
}
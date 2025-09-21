package com.backend.app.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.app.dto.CreateDebtorRequestDTO;
import com.backend.app.dto.DebtorResponseDTO;
import com.backend.app.dto.DebtorPageResponseDTO;
import com.backend.app.entity.Debtor;
import com.backend.app.exception.ApiException;
import com.backend.app.repository.DebtorRepository;
import com.backend.app.service.DebtorService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
public class DebtorServiceImpl implements DebtorService {

    @Autowired
    private DebtorRepository debtorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DebtorResponseDTO createDebtor(CreateDebtorRequestDTO createDebtorRequestDTO) {

        // Check if debtor with same name already exists
        Debtor debtorFromDB = debtorRepository.findByFirstNameAndLastName(
                createDebtorRequestDTO.getFirstName(), 
                createDebtorRequestDTO.getLastName());

        if (debtorFromDB != null) {
            throw new ApiException("Debtor with name " + createDebtorRequestDTO.getFirstName() + 
                    " " + createDebtorRequestDTO.getLastName() + " already exists");
        }

        // Manual mapping to avoid ModelMapper issues
        Debtor debtor = new Debtor();
        debtor.setFirstName(createDebtorRequestDTO.getFirstName());
        debtor.setLastName(createDebtorRequestDTO.getLastName());
        debtor.setEmail(createDebtorRequestDTO.getEmail());
        debtor.setPayerType(createDebtorRequestDTO.getPayerType());
        debtor.setAddress(createDebtorRequestDTO.getAddress());
        debtor.setDateOfBirth(createDebtorRequestDTO.getDateOfBirth());
        debtor.setPayerDetails(createDebtorRequestDTO.getPayerDetails());

        Debtor saved = debtorRepository.save(debtor);

        return modelMapper.map(saved, DebtorResponseDTO.class);
    }


    @Override
    public DebtorPageResponseDTO getAllDebtors(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);
        
        Page<Debtor> debtorsPage = debtorRepository.findAll(pageDetails);
        
        if (debtorsPage.isEmpty()) {
            throw new ApiException("No debtors found");
        }

        List<DebtorResponseDTO> debtorResponseDTOs = debtorsPage.getContent().stream()
                .map(debtor -> modelMapper.map(debtor, DebtorResponseDTO.class))
                .collect(Collectors.toList());

        DebtorPageResponseDTO response = new DebtorPageResponseDTO();
        response.setContent(debtorResponseDTOs);
        response.setPageNo(debtorsPage.getNumber());
        response.setPageSize(debtorsPage.getSize());
        response.setTotalElements(debtorsPage.getTotalElements());
        response.setTotalPages(debtorsPage.getTotalPages());
        response.setLast(debtorsPage.isLast());
        response.setFirst(debtorsPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public DebtorPageResponseDTO getDebtorsByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);

        Page<Debtor> debtorsPage = debtorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword, pageDetails);

        if (debtorsPage.isEmpty()) {
            throw new ApiException("No debtors found with keyword: " + keyword);
        }

        List<DebtorResponseDTO> debtorResponseDTOs = debtorsPage.getContent().stream()
                .map(debtor -> modelMapper.map(debtor, DebtorResponseDTO.class))
                .collect(Collectors.toList());
        
        DebtorPageResponseDTO response = new DebtorPageResponseDTO();
        response.setContent(debtorResponseDTOs);
        response.setPageNo(debtorsPage.getNumber());
        response.setPageSize(debtorsPage.getSize());
        response.setTotalElements(debtorsPage.getTotalElements());
        response.setTotalPages(debtorsPage.getTotalPages());
        response.setLast(debtorsPage.isLast());
        response.setFirst(debtorsPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public DebtorResponseDTO getDebtorById(Long id) {
        Debtor debtor = debtorRepository.findById(id)
                .orElseThrow(() -> new ApiException("Debtor with id " + id + " not found"));
        
        return modelMapper.map(debtor, DebtorResponseDTO.class);
    }
}
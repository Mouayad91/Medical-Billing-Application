package com.backend.app.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.backend.app.entity.ServiceCatalog;
import com.backend.app.repository.ServiceCatalogRepository;
import com.backend.app.service.ServiceCatalogService;
import com.backend.app.dto.ServiceResponseDTO;
import com.backend.app.dto.CreateServiceRequestDTO;
import com.backend.app.dto.ServicePageResponseDTO;
import com.backend.app.exception.ApiException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ServiceCatalogServiceImpl implements ServiceCatalogService {



    @Autowired
    private ServiceCatalogRepository serviceCatalogRepository;


    @Autowired
    private ModelMapper modelMapper;



    @Override
    public ServiceResponseDTO createService(CreateServiceRequestDTO createServiceRequestDTO) {


        ServiceCatalog serviceFromDB = serviceCatalogRepository.findByCode(createServiceRequestDTO.getCode());
        if (serviceFromDB != null) {
            throw new ApiException("Service with code " + createServiceRequestDTO.getCode() + " already exists");
        }

        // Create a new service
        ServiceCatalog newService = new ServiceCatalog();
        newService.setCode(createServiceRequestDTO.getCode());
        newService.setDescription(createServiceRequestDTO.getDescription());
        newService.setBaseFeeCents(createServiceRequestDTO.getBaseFeeCents());

        // Save the new service
        ServiceCatalog savedService = serviceCatalogRepository.save(newService);


        return modelMapper.map(savedService, ServiceResponseDTO.class);

      
    }

    @Override
    public ServicePageResponseDTO getAllServices(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);
        
        Page<ServiceCatalog> servicesPage = serviceCatalogRepository.findAll(pageDetails);
        
        if (servicesPage.isEmpty()) {
            throw new ApiException("No services found");
        }

        List<ServiceResponseDTO> serviceResponseDTOs = servicesPage.getContent().stream()
                .map(service -> modelMapper.map(service, ServiceResponseDTO.class))
                .collect(Collectors.toList());

        ServicePageResponseDTO response = new ServicePageResponseDTO();
        response.setContent(serviceResponseDTOs);
        response.setPageNo(servicesPage.getNumber());
        response.setPageSize(servicesPage.getSize());
        response.setTotalElements(servicesPage.getTotalElements());
        response.setTotalPages(servicesPage.getTotalPages());
        response.setLast(servicesPage.isLast());
        response.setFirst(servicesPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public ServicePageResponseDTO getServicesByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNo, pageSize, sortByAndOrder);

        Page<ServiceCatalog> servicesPage = serviceCatalogRepository.findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageDetails);

        if (servicesPage.isEmpty()) {
            throw new ApiException("No services found with keyword: " + keyword);
        }

        List<ServiceResponseDTO> serviceResponseDTOs = servicesPage.getContent().stream()
                .map(service -> modelMapper.map(service, ServiceResponseDTO.class))
                .collect(Collectors.toList());
        
        ServicePageResponseDTO response = new ServicePageResponseDTO();
        response.setContent(serviceResponseDTOs);
        response.setPageNo(servicesPage.getNumber());
        response.setPageSize(servicesPage.getSize());
        response.setTotalElements(servicesPage.getTotalElements());
        response.setTotalPages(servicesPage.getTotalPages());
        response.setLast(servicesPage.isLast());
        response.setFirst(servicesPage.isFirst());
        response.setSortBy(sortBy);
        response.setSortDir(sortDir);

        return response;
    }

    @Override
    public ServiceResponseDTO getServiceById(Long id) {
        ServiceCatalog service = serviceCatalogRepository.findById(id)
                .orElseThrow(() -> new ApiException("Service with id " + id + " not found"));
        
        return modelMapper.map(service, ServiceResponseDTO.class);
    }



}

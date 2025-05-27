package com.eos.admin.serviceImpl;

import com.eos.admin.dto.DetailedFormDTO;
import com.eos.admin.dto.VendorInfoDTO;
import com.eos.admin.entity.VendorInfo;
import com.eos.admin.repository.VendorInfoRepository;
import com.eos.admin.service.VendorInfoService;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class VendorInfoServiceImpl implements VendorInfoService {

    private final VendorInfoRepository vendorInfoRepository;
    private final ModelMapper modelMapper;
//    private final VendorInfoService vendorInfoService;

    @Autowired
    public VendorInfoServiceImpl(VendorInfoRepository vendorInfoRepository, ModelMapper modelMapper) {
        this.vendorInfoRepository = vendorInfoRepository;
        this.modelMapper = modelMapper;
//        this.vendorInfoService = vendorInfoService;
    }

    @Override
    public VendorInfoDTO saveVendorInfo(VendorInfoDTO vendorInfoDto) {
        try {
            // Mapping DTO to entity using ModelMapper
            VendorInfo vendorInfo = modelMapper.map(vendorInfoDto, VendorInfo.class);
            
            if (vendorInfo.getDirectors() != null) {
                vendorInfo.getDirectors().forEach(director -> director.setVendorInfo(vendorInfo));
            }
            
            if (vendorInfo.getBankDetails() != null) {
                vendorInfo.getBankDetails().setVendorInfo(vendorInfo);
            }

            // Saving the vendor info entity to the database
            VendorInfo savedVendorInfo = vendorInfoRepository.save(vendorInfo);

            // Mapping saved entity back to DTO and returning
            return modelMapper.map(savedVendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while saving VendorInfo: " + e.getMessage(), e);
        }
    }

    @Override
    public VendorInfoDTO getVendorInfoById(Long id) {
        try {
            // Fetching VendorInfo by ID from the repository
            VendorInfo vendorInfo = vendorInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("VendorInfo not found with id: " + id));

            // Mapping entity to DTO and returning
            return modelMapper.map(vendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while fetching VendorInfo by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VendorInfoDTO> getAllVendorInfos() {
        try {
            // Fetching all VendorInfo records from the repository
            List<VendorInfo> vendorInfos = vendorInfoRepository.findAll();

            // Mapping each entity to DTO
            return vendorInfos.stream()
                    .map(vendorInfo -> modelMapper.map(vendorInfo, VendorInfoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while fetching all VendorInfos: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteVendorInfo(Long id) {
        try {
            // Deleting VendorInfo by ID from the repository
            vendorInfoRepository.deleteById(id);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while deleting VendorInfo with id: " + id + " - " + e.getMessage(), e);
        }
    }

	@Override
	public VendorInfoDTO createVendorInfo(DetailedFormDTO detailedFormDTO, MultipartFile chequeImage) throws IOException {
	    log.info("Starting to save vendor details for ID: {}", detailedFormDTO.getId());

     try {
        // Save cheque image and update path
        String savedPath = saveChequeImageAndSetPath(detailedFormDTO, chequeImage);

        // Map form data to DTO
        VendorInfoDTO vendorInfoDto = mapFormToVendorDTO(detailedFormDTO);

        // Convert DTO to Entity
        VendorInfo vendorInfo = modelMapper.map(vendorInfoDto, VendorInfo.class);

        // Set relationships
        associateRelatedEntities(vendorInfo);

        // Persist entity
        VendorInfo savedVendorInfo = vendorInfoRepository.save(vendorInfo);

        log.info("Successfully saved vendor info for ID: {}", savedVendorInfo.getId());

        // Return DTO
        return modelMapper.map(savedVendorInfo, VendorInfoDTO.class);

    } catch (Exception ex) {
        log.error("Error while saving vendor details: {}", ex.getMessage(), ex);
        throw new RuntimeException("Failed to save vendor information", ex);
    }
}

	private String saveChequeImageAndSetPath(DetailedFormDTO detailedFormDTO, MultipartFile chequeImage) throws IOException {
	    if (chequeImage != null && !chequeImage.isEmpty()) {
	        String uploadDir = "uploads/cheque_images/";
	        Files.createDirectories(Paths.get(uploadDir));

	        String filename = System.currentTimeMillis() + "_" + chequeImage.getOriginalFilename();
	        Path filepath = Paths.get(uploadDir, filename);
	        Files.write(filepath, chequeImage.getBytes());

	        String savedPath = filepath.toString();
	        log.info("Saved cheque image to {}", savedPath);

	        if (detailedFormDTO.getBankDetails() != null) {
	            detailedFormDTO.getBankDetails().setChequeImagePath(savedPath);
	        }

	        return savedPath;
	    }

	    log.warn("Cheque image is null or empty, skipping file save.");
	    return null;
	}


private VendorInfoDTO mapFormToVendorDTO(DetailedFormDTO form) {
    VendorInfoDTO dto = new VendorInfoDTO();
    dto.setId(form.getId());
    dto.setCompanyName(form.getCompanyName());
    dto.setAddress(form.getAddress());
    dto.setCity(form.getCity());
    dto.setPinCode(form.getPinCode());
    dto.setTelephone(form.getTelephone());
    dto.setMobile(form.getMobile());
    dto.setEmail(form.getEmail());
    dto.setContactPerson(form.getContactPerson());
    dto.setPan(form.getPan());
    dto.setGst(form.getGst());
    dto.setMsme(form.getMsme());
    dto.setServiceType(form.getServiceType());
    dto.setServiceTypeOther(form.getServiceTypeOther());
    dto.setDeclaration(form.isDeclaration());
    dto.setDirectors(form.getDirectors());
    dto.setBankDetails(form.getBankDetails());
    return dto;
}

private void associateRelatedEntities(VendorInfo vendorInfo) {
    if (vendorInfo.getDirectors() != null) {
        vendorInfo.getDirectors().forEach(director -> director.setVendorInfo(vendorInfo));
    }
    if (vendorInfo.getBankDetails() != null) {
        vendorInfo.getBankDetails().setVendorInfo(vendorInfo);
    }
}
	
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/cheque_images/";
        Files.createDirectories(Paths.get(uploadDir));

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);
        Files.write(filepath, file.getBytes());

        log.info("Saved cheque image to {}", filepath.toString());

        return filepath.toString();
    }

}
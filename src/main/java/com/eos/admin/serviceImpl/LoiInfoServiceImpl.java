package com.eos.admin.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.LoiInfoDTO;
import com.eos.admin.dto.LoiInfoDropDownDTO;
import com.eos.admin.dto.LoiInformationDTO;
import com.eos.admin.dto.NameTypeDTO;
import com.eos.admin.entity.LoiInformationTableEntity;
import com.eos.admin.repository.DesignationRepository;
import com.eos.admin.repository.LocationRepository;
import com.eos.admin.repository.LoiInfoRepository;
import com.eos.admin.service.LoiInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoiInfoServiceImpl implements LoiInfoService {

	private LoiInfoRepository loiInfoRepository;
	private ModelMapper modelMapper;
	private DesignationRepository designationRepository;
	private LocationRepository locationRepository;

	@Autowired
	public LoiInfoServiceImpl(LoiInfoRepository loiInfoRepository, 
			ModelMapper modelMapper,
			DesignationRepository designationRepository,
			 LocationRepository locationRepository
			) {

		this.loiInfoRepository = loiInfoRepository;
		this.modelMapper = modelMapper;
		this.designationRepository = designationRepository;
		this.locationRepository = locationRepository;
		
	}

	@Override
	public LoiInfoDTO createLoiInfo(LoiInfoDTO loiInfoDTO) {

		try {
			// Map DTO to Entity
			LoiInformationTableEntity loiInformationTableEntity = modelMapper.map(loiInfoDTO,
					LoiInformationTableEntity.class);
			log.info("Mapped LoiInfoDTO to LoiInformationTableEntity: {}", loiInformationTableEntity);

			// Save entity to the database
			LoiInformationTableEntity savedEntity = loiInfoRepository.save(loiInformationTableEntity);
			log.info("Saved entity to the database with ID: {}", savedEntity.getId());

			// Map the saved entity back to DTO
			LoiInfoDTO savedLoiInfoDTO = modelMapper.map(savedEntity, LoiInfoDTO.class);
			return savedLoiInfoDTO;
		} catch (Exception e) {
			log.error("Error occurred while creating LoiInfo: {}", e.getMessage(), e);
			throw new RuntimeException("An error occurred while creating LoiInfo", e);
		}
	}

	@Override
	public LoiInfoDTO getAllUniqueValueForDropDown() {

		return null;
	}

	@Override
	public List<LoiInfoDropDownDTO> getAllGradValue(String process, String grade, String companyType) {

		try {
			List<Object[]> getValue = loiInfoRepository.findGridValuesByProcessGradeCompanyType(

					process.trim().toLowerCase(), grade.trim().toLowerCase(), companyType.trim().toLowerCase());
			ArrayList<LoiInfoDropDownDTO> dropdownList = new ArrayList<>();

			for (Object[] row : getValue) {
				LoiInfoDropDownDTO dto = new LoiInfoDropDownDTO();
				dto.setGrid(String.valueOf(row[0]));
				dropdownList.add(dto);
			}
			return dropdownList;
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<LoiInfoDropDownDTO> getAllUniqueDropDown() {

		try {
			List<Object[]> getValue = loiInfoRepository.processAndGradeAndCompanyTypeUnique();
			ArrayList<LoiInfoDropDownDTO> dropdownList = new ArrayList<>();
			for (Object[] row : getValue) {
				LoiInfoDropDownDTO dto = new LoiInfoDropDownDTO();
				dto.setCompanyType(String.valueOf(row[0]));
				dto.setProcess(String.valueOf(row[1]));
				dto.setGrade(String.valueOf(row[2]));

				dropdownList.add(dto);
			}
			return dropdownList;

		} catch (Exception e) {
			log.error("Error occurred while fetching dropdown values: {}", e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@Override
	public LoiInformationDTO getDetailsOfLoiInformation(String gridNo ,String location) {
	    try {
	        LoiInformationTableEntity entity = loiInfoRepository.findDetailsWithGridNo(gridNo);
//	        List<NameTypeDTO>  locationResponse = locationRepository.findNamesByLocationName(location);
	        if (entity == null) {
	            throw new NoSuchElementException("No data found for Grid No: " + gridNo);
	        }

	        LoiInformationDTO loiInformationDTO = modelMapper.map(entity, LoiInformationDTO.class);
	        // Set the dropdown data in DTO
//	        loiInformationDTO.setDepartmentDropdown(locationResponse.stream()
//	                .filter(item -> item.getType().equals("Department"))
//	                .collect(Collectors.toList()));
//
//	        loiInformationDTO.setDesignationDropdown(locationResponse.stream()
//	                .filter(item -> item.getType().equals("Designation"))
//	                .collect(Collectors.toList()));

	        return loiInformationDTO;
	    } catch (NoSuchElementException e) {
	        log.warn("No result found for gridNo {}: {}", gridNo, e.getMessage());
	        throw e; // or return null based on your flow
	    } catch (Exception e) {
	        log.error("Error occurred while mapping LoiInformation: {}", e.getMessage(), e);
	        throw new RuntimeException("An error occurred while fetching LOI information", e);
	    }
	}

	@Override
	public List<NameTypeDTO> getNamesByLocation(String location) {
		return locationRepository.findNamesByLocationName(location);
	}


}

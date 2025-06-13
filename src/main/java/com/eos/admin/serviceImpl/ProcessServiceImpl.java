package com.eos.admin.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.ProcessDTO;
import com.eos.admin.dto.ProcessDropdownDTO;
import com.eos.admin.entity.ProcessEntity;
import com.eos.admin.exception.ProcessException;
import com.eos.admin.repository.ProcessRepository;
import com.eos.admin.service.ProcessService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

	private final ProcessRepository processRepository;
	private final ModelMapper modelMapper;

	public ProcessServiceImpl(ProcessRepository processRepository, ModelMapper modelMapper) {
		this.processRepository = processRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ProcessDTO> saveProcessInformation(List<ProcessDTO> processDTOList) {
		log.info("Saving process information: {}", processDTOList);

		if (processDTOList == null || processDTOList.isEmpty()) {
			log.error("Invalid process data: list is null or empty");
			throw new ProcessException("Process list cannot be null or empty");
		}
		try {
			List<ProcessEntity> entitiesToSave = processDTOList.stream().peek(dto -> {
				if (dto.getProcessName() == null) {
					throw new ProcessException("Process name cannot be null");
				}
			}).map(dto -> modelMapper.map(dto, ProcessEntity.class)).collect(Collectors.toList());

			// Save all entities
			List<ProcessEntity> savedEntities = processRepository.saveAll(entitiesToSave);

			// Map saved entities back to DTOs
			List<ProcessDTO> savedDTOs = savedEntities.stream().map(entity -> modelMapper.map(entity, ProcessDTO.class))
					.collect(Collectors.toList());

			log.info("Processes saved: {}", savedDTOs);
			return savedDTOs;
		} catch (Exception e) {
			log.error("Error saving processes", e);
			throw new ProcessException("Failed to save process list", e);
		}
	}

	@Override
	public List<ProcessDropdownDTO> getDropdownList() {
		try {
			log.info("Fetching all processes for dropdown.");
			List<ProcessEntity> entities = processRepository.findAll();

			List<ProcessDropdownDTO> dropdownList = entities.stream().map(p -> {
				String formattedName = p.getProcessName() + "-" + String.format("%03d", p.getProcessCode());
				log.debug("Formatted process: {}", formattedName);
				return new ProcessDropdownDTO(formattedName);
			}).collect(Collectors.toList());

			log.info("Successfully formatted {} process entries.", dropdownList.size());
			return dropdownList;
		} catch (Exception e) {
			log.error("Error fetching process dropdown list: {}", e.getMessage(), e);
			throw new RuntimeException("Unable to fetch process list", e);
		}
	}

	@Override
	public List<ProcessDropdownDTO> getDropdownListRegister() {

		try {
			List<ProcessEntity> entities = processRepository.findAll();
			List<ProcessDropdownDTO> dropDownList = entities.stream()
					.map(entity -> new ProcessDropdownDTO(entity.getProcessName(), entity.getProcessCode()))
					.collect(Collectors.toList());
			log.info("Successfully formatted {} process entries.", dropDownList.size());
			return dropDownList;
		} catch (Exception e) {
			log.error("Error fetching process dropdown list: {}", e.getMessage(), e);
			throw new RuntimeException("Unable to fetch process list", e);
		}

	}

}

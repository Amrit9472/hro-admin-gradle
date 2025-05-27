package com.eos.admin.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.CandidatesDTO;
import com.eos.admin.entity.CandidatesEntity;
import com.eos.admin.repository.CandidatesRepository;
import com.eos.admin.service.CandidatesService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandidatesServiceImpl implements CandidatesService {

	private CandidatesRepository candidatesRepository;
	private ModelMapper modelMapper;

	@Autowired
	public CandidatesServiceImpl(CandidatesRepository candidatesRepository,ModelMapper modelMapper) {
		super();
		this.candidatesRepository = candidatesRepository;
		this.modelMapper=modelMapper;
	}

	@Override
	public CandidatesDTO saveCandidates(CandidatesDTO candidatesDTO) {
		    CandidatesEntity candidatesEntity = modelMapper.map(candidatesDTO, CandidatesEntity.class);
	        CandidatesEntity savedEntity = candidatesRepository.save(candidatesEntity);
	        return modelMapper.map(savedEntity, CandidatesDTO.class);
	}

	@Override
	public List<CandidatesDTO> saveCandidatesMultiple(List<CandidatesDTO> candidatesDTO) {
        try {
            log.info("Saving multiple candidates: {}", candidatesDTO.size());

            // Convert DTOs to Entities
            List<CandidatesEntity> entities = candidatesDTO.stream()
                    .map(dto -> modelMapper.map(dto, CandidatesEntity.class))
                    .collect(Collectors.toList());

            // Save all entities
            List<CandidatesEntity> savedEntities = candidatesRepository.saveAll(entities);

            // Convert back to DTOs
            List<CandidatesDTO> savedDTOs = savedEntities.stream()
                    .map(entity -> modelMapper.map(entity, CandidatesDTO.class))
                    .collect(Collectors.toList());

            log.info("Successfully saved {} candidates", savedDTOs.size());
            return savedDTOs;
        } catch (Exception e) {
            log.error("Error while saving candidates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save candidates", e);
        }
    }


}

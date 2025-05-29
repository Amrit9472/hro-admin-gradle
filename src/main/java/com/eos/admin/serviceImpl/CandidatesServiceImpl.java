package com.eos.admin.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.CandidatesDTO;
import com.eos.admin.entity.CandidatesEntity;
import com.eos.admin.repository.CandidatesRepository;
import com.eos.admin.service.CandidatesService;

@Service
public class CandidatesServiceImpl implements CandidatesService {

    private final CandidatesRepository candidatesRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CandidatesServiceImpl(CandidatesRepository candidatesRepository, ModelMapper modelMapper) {
        this.candidatesRepository = candidatesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CandidatesDTO saveCandidates(CandidatesDTO candidatesDTO) {
        // Check if candidate with same email and scheme already exists
        Optional<CandidatesEntity> existingCandidate = candidatesRepository.findByCandiEmailAndScheme(candidatesDTO.getCandiEmail(), candidatesDTO.getScheme());
        if (existingCandidate.isPresent()) {
            throw new RuntimeException("Candidate with the same email and scheme already exists.");
        }

        // Map DTO to Entity and save it
        CandidatesEntity candidatesEntity = modelMapper.map(candidatesDTO, CandidatesEntity.class);
        CandidatesEntity savedEntity = candidatesRepository.save(candidatesEntity);

        // Convert back the saved entity to DTO and return
        return modelMapper.map(savedEntity, CandidatesDTO.class);
    }

    @Override
    public List<CandidatesDTO> saveCandidatesMultiple(List<CandidatesDTO> candidatesDTOList) {
        // Validate each candidate for duplicate email and scheme
        for (CandidatesDTO candidateDTO : candidatesDTOList) {
            Optional<CandidatesEntity> existingCandidate = candidatesRepository.findByCandiEmailAndScheme(candidateDTO.getCandiEmail(), candidateDTO.getScheme());
            if (existingCandidate.isPresent()) {
                throw new RuntimeException("Candidate with the same email and scheme already exists.");
            }
        }

        // Convert DTOs to Entities
        List<CandidatesEntity> entities = candidatesDTOList.stream()
                .map(dto -> modelMapper.map(dto, CandidatesEntity.class))
                .collect(Collectors.toList());

        // Save all entities
        List<CandidatesEntity> savedEntities = candidatesRepository.saveAll(entities);

        // Convert back to DTOs and return
        return savedEntities.stream()
                .map(entity -> modelMapper.map(entity, CandidatesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CandidatesDTO> findBySubmittedDateBetweenAndVendorEmail(LocalDateTime start, LocalDateTime end, String vendorEmail) {
        List<CandidatesEntity> entities = candidatesRepository.findBySubmittedDateBetweenAndVendorEmail(start, end, vendorEmail);
        return entities.stream()
                .map(entity -> modelMapper.map(entity, CandidatesDTO.class))
                .collect(Collectors.toList());
    }
}

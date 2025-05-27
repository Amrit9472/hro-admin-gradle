package com.eos.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.CandidatesDTO;
import com.eos.admin.serviceImpl.CandidatesServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/candi")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class CandidatesController {

	private final CandidatesServiceImpl candidatesService;

	@Autowired
	public CandidatesController(CandidatesServiceImpl candidatesService) {
		super();
		this.candidatesService = candidatesService;
	}

	@PostMapping
	public ResponseEntity<CandidatesDTO> createCandidate(@RequestBody CandidatesDTO candidatesDTO) {

		try {
			if(candidatesDTO == null  ) {
				log.warn("Candidate request is null {}",candidatesDTO);
				return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
			}
			CandidatesDTO request = candidatesService.saveCandidates(candidatesDTO);
			return new ResponseEntity<>(request, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error occurred while creating employee: {}", e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/bulk")
	public ResponseEntity<List<CandidatesDTO>> createCandidateMultiple(@RequestBody List<CandidatesDTO> candidatesDTO) {

		try {
			if(candidatesDTO == null || candidatesDTO.isEmpty() ) {
				log.warn("Candidate request is null {}",candidatesDTO);
				return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
			}
			List<CandidatesDTO> savedCandidates = candidatesService.saveCandidatesMultiple(candidatesDTO);
			return new ResponseEntity<>(savedCandidates, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error occurred while creating employee: {}", e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

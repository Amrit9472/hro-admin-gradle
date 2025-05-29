package com.eos.admin.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.CandidatesDTO;
import com.eos.admin.serviceImpl.CandidatesServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/candi")
@Slf4j
@CrossOrigin(origins = "*")
public class CandidatesController {

    private final CandidatesServiceImpl candidatesService;

    @Autowired
    public CandidatesController(CandidatesServiceImpl candidatesService) {
        this.candidatesService = candidatesService;
    }

    @PostMapping
    public ResponseEntity<CandidatesDTO> createCandidate(@RequestBody CandidatesDTO candidatesDTO) {

        try {
            if (candidatesDTO == null) {
                log.warn("Candidate request is null {}", candidatesDTO);
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            CandidatesDTO request = candidatesService.saveCandidates(candidatesDTO);
            return new ResponseEntity<>(request, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating candidate: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CandidatesDTO>> createCandidateMultiple(@RequestBody List<CandidatesDTO> candidatesDTO) {

        try {
            if (candidatesDTO == null || candidatesDTO.isEmpty()) {
                log.warn("Candidate request is null {}", candidatesDTO);
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            List<CandidatesDTO> savedCandidates = candidatesService.saveCandidatesMultiple(candidatesDTO);
            return new ResponseEntity<>(savedCandidates, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating candidates: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byDateRange")
    public ResponseEntity<List<CandidatesDTO>> getCandidatesByDateRange(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "email") String vendorEmail) {

        try {
            // Parse the date strings to LocalDate
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            // Convert to LocalDateTime (start of the day for startDate, end of the day for endDate)
            LocalDateTime startOfDay = start.atStartOfDay();
            LocalDateTime endOfDay = end.atTime(23, 59, 59);

            // Fetch candidates using the existing service method
            List<CandidatesDTO> candidates = candidatesService.findBySubmittedDateBetweenAndVendorEmail(startOfDay, endOfDay, vendorEmail);
            return new ResponseEntity<>(candidates, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching candidates by date range and email: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/manager-status")
    public ResponseEntity<String> getManagerStatus(@RequestParam(name = "email") String email) {
        try {
            String managerStatus = candidatesService.getManagerStatusByEmail(email);

            if (managerStatus != null) {
                return new ResponseEntity<>(managerStatus, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Manager status not found for the provided email.", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            log.error("Error occurred while fetching manager status for email: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error occurred while fetching manager status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

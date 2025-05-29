package com.eos.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.OurEmployeeDTO;
import com.eos.admin.entity.OurEmployees;
import com.eos.admin.service.OurEmployeeService;
import com.eos.admin.serviceImpl.OurEmployeeServiceImpl;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/ourEmployee")
public class OurEmployeeController {

	private OurEmployeeServiceImpl ourEmployeeService;
	private ModelMapper modelMapper;

	@Autowired
	public OurEmployeeController(OurEmployeeServiceImpl ourEmployeeService, ModelMapper modelMapper) {
		this.ourEmployeeService = ourEmployeeService;
		this.modelMapper = modelMapper;
	}

	@PostMapping("/{id}")
	public ResponseEntity<?> saveOurEmployee(@RequestBody OurEmployeeDTO ourEmployeeDTO, @PathVariable("id") Long id) {
		log.info("Request received to save our employee: {}", ourEmployeeDTO);
		try {
			ByteArrayResource savedEmployeeDTO = ourEmployeeService.saveOurEmployee(ourEmployeeDTO, id);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_details.pdf");
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
			return ResponseEntity.ok().headers(headers).body(savedEmployeeDTO);
		} catch (Exception e) {
			log.error("Error saving employee: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving employee. Please try again later.");
		}
	}

	@GetMapping("/{location}")
	public ResponseEntity<?> getAllLoiGeneratedEmployee(@PathVariable("location") String location){
		try {
			List<OurEmployeeDTO>  getOurEmployeeDetails = ourEmployeeService.getAllLoiGeneratedEmployee(location);
			return ResponseEntity.ok(getOurEmployeeDetails);
		} catch (Exception e) {
			log.error("Error fetching employee: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Failed to fetch employee details");
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

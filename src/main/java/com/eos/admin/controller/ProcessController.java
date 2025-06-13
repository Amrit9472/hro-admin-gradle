package com.eos.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.ProcessDTO;
import com.eos.admin.dto.ProcessDropdownDTO;
import com.eos.admin.exception.ProcessException;
import com.eos.admin.service.ProcessService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/process")
@CrossOrigin("http://localhost:5173")
public class ProcessController {

	private final ProcessService processService;

	public ProcessController(ProcessService processService) {
		super();
		this.processService = processService;
	}

	@PostMapping
	public ResponseEntity<List<ProcessDTO>> createProcessInformation(@RequestBody List<ProcessDTO> processDTO) {
		log.info("Received request to create process: {}", processDTO);
		try {
			List<ProcessDTO> result = processService.saveProcessInformation(processDTO);
			log.info("Process creation successful: {}", result);
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} catch (ProcessException ex) {
			log.error("Process creation failed: {}", ex.getMessage());
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			log.error("Unexpected error occurred", ex);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @GetMapping("/dropdown")
	    public ResponseEntity<List<ProcessDropdownDTO>> getProcessDropdown() {
	        log.info("Received request for process dropdown.");
	        List<ProcessDropdownDTO> list = processService.getDropdownList();
	        return ResponseEntity.ok(list);
	    }
	 
	 @GetMapping("/dropdownRegister")
	    public ResponseEntity<List<ProcessDropdownDTO>> getProcessDropdownRegister() {
	        log.info("Received request for process dropdown.");
	        List<ProcessDropdownDTO> list = processService.getDropdownListRegister();
	        return ResponseEntity.ok(list);
	    }

}

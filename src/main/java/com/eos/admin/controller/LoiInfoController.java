package com.eos.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.LoiInfoDTO;
import com.eos.admin.dto.LoiInfoDropDownDTO;
import com.eos.admin.dto.LoiInformationDTO;
import com.eos.admin.dto.NameTypeDTO;
import com.eos.admin.exception.InvalidInputException;
import com.eos.admin.service.LoiInfoService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/loi")
public class LoiInfoController {

	private LoiInfoService loiInfoService;

	public LoiInfoController(LoiInfoService loiInfoService) {
		super();
		this.loiInfoService = loiInfoService;
	}

	@PostMapping("/createLoiInfo")
	public ResponseEntity<?> createLoiInfoGrid(@RequestBody LoiInfoDTO loiInfoDTO) {
		try {
			if (loiInfoDTO == null) {
				log.error("Invalid input: loiInfoDTO is null");
				throw new InvalidInputException("loiInfo can have some date");
			}
			log.info("Received LoiInfoDTO: {}", loiInfoDTO);
			LoiInfoDTO saveLoiInfoData = loiInfoService.createLoiInfo(loiInfoDTO);
			log.info("Successfully saved LoiInfo data");
			return new ResponseEntity<>(saveLoiInfoData, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("An error occurred while processing the LoiInfo: {}", e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropdownValues")
	public ResponseEntity<?> getAllUniqueValueFordropdown() {
		try {
			log.info("Received Request fetch dropdown values");
			LoiInfoDTO getValueFordropdown = loiInfoService.getAllUniqueValueForDropDown();
			return new ResponseEntity<>(getValueFordropdown, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occurred while processing the LoiInfo: {}", e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/filter")
	public ResponseEntity<?> getGridValue(@RequestParam("process") String process, @RequestParam("grade") String grade,
			@RequestParam("companyType") String companyType) {
		// Log incoming parameters
		log.info("Received parameters - Process: {}, Grade: {}, Company Type: {}", process, grade, companyType);

		try {
			String formattedProcessName = process.replaceFirst("-\\d+$", "");
			List<LoiInfoDropDownDTO> getGradValue = loiInfoService.getAllGradValue(formattedProcessName, grade, companyType);
			return new ResponseEntity<>(getGradValue, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occurred while processing the LoiInfo: {}", e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/dropDown")
	public ResponseEntity<?> getDropDownValues() {
		try {
			List<LoiInfoDropDownDTO> dropDownValue = loiInfoService.getAllUniqueDropDown();
			return new ResponseEntity<>(dropDownValue, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occurred while processing the LoiInfo: {}", e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while processing the request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
   @GetMapping("/getDetailswithGridNo/{gridNo}/{location}")
   public ResponseEntity<?> getDetailsOfLoiInformation(@PathVariable("gridNo") String gridNo, 
		   @PathVariable("location") String location){
	   log.info("Received request for get information with Grid:{}",gridNo);
	   try {
		 
		LoiInformationDTO  request = loiInfoService.getDetailsOfLoiInformation(gridNo,location);
		return new ResponseEntity<>(request,HttpStatus.OK);
	} catch (Exception e) {
       log.error("An error occurred while processing the loi information with grid no");
       return new ResponseEntity<>("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
	}
   }

 @GetMapping("/names-by-location/{location}")
 public List<NameTypeDTO> getNamesByLocation(@PathVariable("location") String location) {
     return loiInfoService.getNamesByLocation(location);
 }
}

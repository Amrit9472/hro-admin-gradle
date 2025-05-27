package com.eos.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.DetailedFormDTO;
import com.eos.admin.dto.VendorInfoDTO;
import com.eos.admin.service.VendorInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/vendorInfo")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class VendorInfoController {

	private final VendorInfoService vendorInfoService;

	public VendorInfoController(VendorInfoService vendorInfoService) {
		this.vendorInfoService = vendorInfoService;
	}

	// Updated submit method to handle multipart/form-data
	@PostMapping(value = "/createVendorInfo", consumes = { "multipart/form-data" })
	public ResponseEntity<VendorInfoDTO> submitDetailedForm(@RequestPart("form") DetailedFormDTO detailedFormDTO,
			@RequestPart(value = "chequeImage", required = false) MultipartFile chequeImage) {

		log.info("Received form submission: {}", detailedFormDTO);

		try {
			if (chequeImage == null && chequeImage.isEmpty()) {
				log.warn("Vendor request data not recived {} {}", detailedFormDTO, chequeImage);
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

			}
			VendorInfoDTO request = vendorInfoService.createVendorInfo(detailedFormDTO, chequeImage);

			log.info("Successfully created vendor with ID: {}", request.getId());
			return new ResponseEntity<>(request, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error("Form submission error: {}", e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get single vendor by ID
	@GetMapping("/{id}")
	public ResponseEntity<VendorInfoDTO> getVendorInfoById(@PathVariable Long id) {
		try {
			VendorInfoDTO vendorInfo = vendorInfoService.getVendorInfoById(id);
			return ResponseEntity.ok(vendorInfo);
		} catch (Exception e) {
			log.error("Error fetching vendor by ID {}: {}", id, e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	// Get all vendor entries
	@GetMapping("/list")
	public ResponseEntity<List<VendorInfoDTO>> getAllVendorInfos() {
		try {
			List<VendorInfoDTO> vendors = vendorInfoService.getAllVendorInfos();
			return ResponseEntity.ok(vendors);
		} catch (Exception e) {
			log.error("Error fetching vendors: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	// Delete vendor by ID
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteVendor(@PathVariable Long id) {
		try {
			vendorInfoService.deleteVendorInfo(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			log.error("Error deleting vendor with ID {}: {}", id, e.getMessage());
			return ResponseEntity.internalServerError().body("Delete failed: " + e.getMessage());
		}
	}

	// Helper to save file on local disk
}
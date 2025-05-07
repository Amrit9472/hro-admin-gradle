package com.eos.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.entity.Designation;
import com.eos.admin.service.DesignationService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/designations")
public class DesignationController {
	
	
	private DesignationService designationService;
	
	@Autowired
	public DesignationController(DesignationService designationService) {
		super();
		this.designationService = designationService;
	}


	@PostMapping
	public ResponseEntity<Designation> createDesignation(@RequestBody Designation designation) {

		Designation saveDesignation = designationService.saveDesignation(designation);
		return new ResponseEntity<>(saveDesignation , HttpStatus.CREATED);
	}
	

}

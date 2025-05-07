package com.eos.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.entity.Department;
import com.eos.admin.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	 private DepartmentService departmentService;
	 
	 @Autowired
	 public DepartmentController(DepartmentService departmentService) {
		super();
		this.departmentService = departmentService;
	}


	@PostMapping
	 public ResponseEntity<Department> createDepartment(@RequestBody Department department) {

		Department saveDepartment = departmentService.saveDepartment(department);
	 	return new ResponseEntity<>(saveDepartment, HttpStatus.CREATED);
	 	
	 }
	 
	 
}

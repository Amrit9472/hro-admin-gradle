package com.eos.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.ChangePasswordRequest;
import com.eos.admin.dto.ReqRes;
import com.eos.admin.serviceImpl.UserManagementService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class UserManagementController {
	
	@Autowired
	private UserManagementService userManagementService;
	
	@PostMapping("/register")
	public ResponseEntity<ReqRes> register(@RequestBody ReqRes reqRes){
		ReqRes response = userManagementService.register(reqRes);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
		ReqRes response = userManagementService.login(req);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/admin/get-process-name")
	public ResponseEntity<List<String>> getAllProcess(){
		return ResponseEntity.ok(userManagementService.getMyProcessName());
	}

	@PostMapping("/change-password")
	public ResponseEntity<ReqRes> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
		return ResponseEntity.ok(userManagementService.changePassword(changePasswordRequest));
	}
}

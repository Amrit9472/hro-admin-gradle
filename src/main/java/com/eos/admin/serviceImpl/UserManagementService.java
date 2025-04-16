package com.eos.admin.serviceImpl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.ReqRes;
import com.eos.admin.entity.OurUsers;
import com.eos.admin.jwt.JWTUtilsImpl;
import com.eos.admin.repository.UsersRepository;

@Service
public class UserManagementService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder passwordEncode;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtilsImpl jwtUtilsImpl;

	public ReqRes register(ReqRes registrationRequest) {
		ReqRes resp = new ReqRes();
		try {
			// checking user having unique id
			String email = registrationRequest.getEmail();
			if (email != null && !email.isEmpty()) {
				Optional<OurUsers> existingUser = usersRepository.findByEmail(email);

				if (existingUser.isPresent()) {
					resp.setStatusCode(400);
					resp.setError("Emp id is already in use");
					return resp;
				}
			} else {
				resp.setStatusCode(400);
				resp.setError("Emp id cann't be empty");
			}

			// Create and populate the new user object
			OurUsers ourUsers = new OurUsers();
			ourUsers.setEmail(email);
			ourUsers.setCity(registrationRequest.getCity());
			ourUsers.setRole(registrationRequest.getRole());
			ourUsers.setName(formattedString(registrationRequest.getName()));
			String processName = registrationRequest.getProcess().trim().toUpperCase();
			String processCode = registrationRequest.getProcessCode().trim();
			ourUsers.setProcess(processName);
			ourUsers.setProcessCode(processCode);
			
			String uniqueCode = processName +"-"+processCode;
			ourUsers.setUniqueCode(uniqueCode);
			
			ourUsers.setPassword(passwordEncode.encode(registrationRequest.getPassword()));

			OurUsers ourUsersResult = usersRepository.save(ourUsers);
			if (ourUsersResult.getId() > 0) {
				resp.setOurUsers(ourUsersResult);
				resp.setMessage("User save sucessfully");
				resp.setStatusCode(200);
			}
		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError("An error occurred: " + e.getMessage());
		}
		return resp;
	}

	public ReqRes login(ReqRes loginRequest) {
		ReqRes response = new ReqRes();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			var user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
			var jwt = jwtUtilsImpl.generateToken(user);
			var refreshToken = jwtUtilsImpl.generateRefreshToken(new HashMap<>(), user);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRole(user.getRole());
//			response.setProcess(user.getProcess());
			response.setName(user.getName());
			response.setEmail(user.getEmail());
			response.setRefreshToken(refreshToken);
			response.setCity(user.getCity());
			response.setExpirationTime("24Hrs");
			response.setMessage("SuccessFull login ");
			response.setUniqueCode(user.getUniqueCode());
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("Incorrect Password");
		}
		return response;
	}

	private String formattedString(String string) {

		if (string != null && !string.isEmpty()) {
			String formattedResponse = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
		}
		return string;

	}

}

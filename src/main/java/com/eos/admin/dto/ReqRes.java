package com.eos.admin.dto;

import java.util.List;

import com.eos.admin.entity.OurUsers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

	private int statusCode;
	private String error;
	private String message;
	private String token;
	private String refreshToken;
	private String expirationTime;
	private String name;
	private String city;
	private String role;
	private String email;
	private String password;
	private String  process;
	private String processCode;
	private String uniqueCode;
	private OurUsers ourUsers;
	private List<OurUsers> ourUserList;

}

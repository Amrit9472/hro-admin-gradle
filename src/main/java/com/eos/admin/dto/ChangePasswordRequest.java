package com.eos.admin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePasswordRequest {
	
	private String email;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;

}

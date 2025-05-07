package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsOnManagerPageDTO {

	private Long id;
	private String fullName;
	private String email;
	private String jobProfile;
	private Long mobileNo;
	private String gender;
	private String processesStatus;
	private Date creationDate;
	

	
}

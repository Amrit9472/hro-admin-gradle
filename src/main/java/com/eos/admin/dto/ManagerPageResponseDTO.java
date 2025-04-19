package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerPageResponseDTO {

	private Long id;
	private String fullName;
	private String email;
	private String jobProfile;
	private String mobileNo;
	private Date creationDate;
}

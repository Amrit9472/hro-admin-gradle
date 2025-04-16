package com.eos.admin.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExcelReportDto {
	private Long id;
	private String fullName;
	private String email;
	private String jobProfile;
	private String Qualification;
	private Long mobileNo;
	private String permanentAddress;
	private String currentAddress;
	private String gender;
	private String previousOrganisation;
	private Date dob;
	private String maritalStatus;
	private String refferal;
	private String aadhaarNumber;
	private String  languages;
	private Float experience;
	private String source;
	private String subSource;
	private String initialStatus;
	private String processesStatus;
	private String hrStatus;
	private String managerStatus;
	private Date creationDate;
	private String lastInterviewAssin;
	private String reMarksByHr;
	private String reMarksByManager;
	private String profileScreenRemarks;
	private String status;
	private String hrName;
	private String remarksOnEveryStages;
	private Date changesDateTime;
}

package com.eos.admin.dto;

import java.util.Date;
import java.util.List;

import com.eos.admin.entity.InterviewProcesses;
import com.eos.admin.entity.StatusHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

	private Long id;
	private String fullName;
	private String email;
	private String jobProfile;
	private String Qualification;
	private String Stream;
	private Long mobileNo;
	private String permanentAddress;
	private String currentAddress;
	private String gender;
	private String previousOrganisation;
	private String workExp;
	private Date dob;
	private String maritalStatus;
	private String refferal;
	private String aadharFilename;
	private String initialStatus;
	private String processesStatus;
	private String hrStatus;
	private String managerStatus;
	private List<StatusHistory> statusHistories;
	private List<InterviewProcesses> interviewProcesses;
	private String aadhaarNumber;
	private List<LanguageDTO> language;
	private List<String> writeLanguages;
	private Float experience;
	private String source;
	private String subSource;
	private Date creationDate;
	private String lastInterviewAssin;
	private String appliedLocation;

}

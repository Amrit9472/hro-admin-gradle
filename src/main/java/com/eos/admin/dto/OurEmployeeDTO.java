package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OurEmployeeDTO {
    private Long employeeId;
	private String officialEmail;
	private String employeeCode;
	private Date joiningDate;
	private String department;
	private String designation;
	private String process;
	private String CompanyType;
	private String grid;
	private String grade;
	private Double ctc;
	private Double takeHome;
	private String trainingApplicable;
	private Long trainingDays;
	
}

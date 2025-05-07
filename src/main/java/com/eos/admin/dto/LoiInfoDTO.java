package com.eos.admin.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoiInfoDTO {

	private Long id;
	private String grid;
	private String process;
	private String grade;
	private String companyType;
	private String designation;
	private String appenticeEnrollment;
	private String aitmEnrollment;
	private String trainingApplicable;
	private String trainingDays;
	private Date gridGeneration;
	private Double ctc;
	private Double takeHome;
	private Double loyaltyBonus;
	private Double pli;
}

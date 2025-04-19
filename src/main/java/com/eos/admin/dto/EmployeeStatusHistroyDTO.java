package com.eos.admin.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeStatusHistroyDTO {
	
	private Long id;
	private String fullName;
	private String email;
	private Date creationDate;
	private String aadhaarNumber;
	private List<StatusHistoryDTO> statusHistory;
	
}

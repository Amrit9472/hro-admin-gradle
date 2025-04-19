package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequestDTO {
  
	private Long id;
	private String newStatus;
	private String responseSubmitbyName;
	private String remarks;
	private String processName;
	private String jobProfile;
	private String clientRound;
}

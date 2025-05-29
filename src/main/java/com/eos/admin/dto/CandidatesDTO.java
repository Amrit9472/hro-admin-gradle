package com.eos.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatesDTO {

	private String candiName;
	private String candiMobile;
	private String candiEmail;
	
	private LocalDateTime submittedDate;
	
	private String vendorEmail;
	
	private String scheme;
}

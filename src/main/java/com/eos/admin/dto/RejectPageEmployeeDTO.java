package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectPageEmployeeDTO {
 
	
	    private Long id;
	    private String fullName;
	    private String email;
	    private String jobProfile;
	    private Date creationDate;
	    
}

package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorQueryDTO {
	
	private Long id;
	private String vendorEmail;    
    private String queryText;
}


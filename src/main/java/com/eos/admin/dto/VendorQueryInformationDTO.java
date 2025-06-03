package com.eos.admin.dto;

import com.eos.admin.enums.VendorStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorQueryInformationDTO {
	
	private Long id;
	private String vendorEmail;
	private String queryText;
	private VendorStatusType vendorQueryStatus;
	private String inProgressRemark;
    private String closedRemarks;
}

package com.eos.admin.dto;

import com.eos.admin.enums.VendorStatusType;

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
	private VendorStatusType vendorQueryStatus;
	private String remark;
    private String closedRemarks;
	public VendorQueryDTO(Long id, String vendorEmail, String queryText) {
		super();
		this.id = id;
		this.vendorEmail = vendorEmail;
		this.queryText = queryText;
	}

}

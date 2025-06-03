package com.eos.admin.dto;

import com.eos.admin.enums.VendorDetailsVerification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorVerificationDTO {

	private VendorDetailsVerification vendorDetailsVerification;
	private String remark;
	private boolean verification;
}

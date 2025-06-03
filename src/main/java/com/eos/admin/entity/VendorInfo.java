package com.eos.admin.entity;

import java.util.List;

import com.eos.admin.enums.VendorDetailsVerification;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendor_info")
public class VendorInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String companyName;
	private String address;
	private String city;
	private String pinCode;
	private String telephone;
	private String mobile;
	private String email;
	private String contactPerson;
	private String pan;
	private String gst;
	private String msme;
	private String serviceType;
	private String serviceTypeOther;
	private boolean declaration;
	
	@Enumerated(EnumType.STRING)
	private VendorDetailsVerification vendorDetailsVerification;
	
	@Column(name = "verificationRemark")
	private String verificationRemark;
	
	@Column(name = "verification")
	private boolean verification;

	@OneToMany(mappedBy = "vendorInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Director> directors;

	@OneToOne(mappedBy = "vendorInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private BankDetails bankDetails;
}
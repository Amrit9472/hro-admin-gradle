package com.eos.admin.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_details")
public class BankDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String bankName;
    private String accountName;
    private String accountNumber;
    private String ifscCode;
    private String branchAddress;

    private String chequeImagePath;

    @ManyToOne
    @JoinColumn(name = "vendorid", referencedColumnName = "id", nullable = false)  // Foreign key
    private VendorInfo vendorInfo;
    
}
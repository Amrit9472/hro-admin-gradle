package com.eos.admin.entity;

import com.eos.admin.enums.VendorStatusType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VendorQuery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String vendorEmail;
	private String queryText;
	
    @Enumerated(EnumType.STRING)
	private VendorStatusType vendorQueryStatus;
    
    @Column(name = "inProgressRemark")
    private String inProgressRemark;
    
    @Column(name = "closedRemark")
    private String closedRemark;
	
    public VendorQuery(String vendorEmail, String queryText) {
        this.vendorEmail = vendorEmail;
        this.queryText = queryText;
    }
}

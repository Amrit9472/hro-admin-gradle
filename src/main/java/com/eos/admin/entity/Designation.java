package com.eos.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "designations")
public class Designation {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "designationId")
    private Long  designationId;
	
	
	@Column(name = "designationCode")
    private String designationCode;
	
	@Column(name = "designationName")
    private String designationName;
	

	@Column(name = "locationId")
	private String locationId;
	
	@Column(name = "category")
    private String category;
	
	@Column(name = "salaryCategory")
    private String salaryCategory;
	
	@Column(name = "loyaltyBonusApplicable")
    private String loyaltyBonusApplicable;    
}

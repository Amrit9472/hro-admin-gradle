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

@Entity
@Table(name = "department")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Department {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "departmentId")
	private Long departmentId;
	
	@Column(name = "departmentCode")
	private String departmentCode;
	
	@Column(name = "departmentName")
	private String departmentName;
	
	
	@Column(name = "locationId")
	private String locationId;
	
	@Column(name = "departmentHead")
	private String departmentHead;
	

}

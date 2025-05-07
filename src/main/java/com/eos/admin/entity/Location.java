package com.eos.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Location {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="location_id")
	private  String    locationId;
	
	@Column(name = "loaction_name")
	private String locationName;
	
	@Column(name = "branch")
	private String branchName;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "address")
	private String address;
}

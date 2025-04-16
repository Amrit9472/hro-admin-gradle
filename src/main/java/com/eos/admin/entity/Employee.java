package com.eos.admin.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "email", nullable = false , unique = true)
	private String email;
	
	@Column(name = "job_profile")
	private String jobProfile;

	@Column(name = "Qualification")
	private String Qualification;

	@Column(name = "mobile_no")
	private Long mobileNo;

	@Column(name = "permanent_address")
	private String permanentAddress;

	@Column(name = "current_address")
	private String currentAddress;

	@Column(name = "gender")
	private String gender;

	@Column(name = "previous_Organisation")
	private String previousOrganisation;
	
	@Column(name = "dob")
	private Date dob;

	@Column(name = "marital_status")
	private String maritalStatus;
	
	@Column(name = "refferal")
	private String refferal;
	
	@Column(name = "aadhaar_number",nullable = false, unique = true)
	private String aadhaarNumber;
	
	@Column(name = "languages")
	private String  languages;
	
	@Column(name ="experience")
	private Float experience;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "sub_source")
	private String subSource;

	@Column(name = "aadhar_filename")
	private String aadharFilename;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<StatusHistory> statusHistories;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InterviewProcesses> interviewProcesses;
	
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmployeeStatusDetails employeeStatusDetails;
	
//	@Column(name = "initial_status")
//	private String initialStatus;
//	
//	@Column(name = "processes_status")
//	private String processesStatus;
//	
//	@Column(name = "hr_status")
//	private String hrStatus;
	
//	@Column(name = "manager_status")
//	private String managerStatus;
	
//	@Column(name = "last_Interview_Assin")
//	private String lastInterviewAssin;
//	
//	@Column(name = "reMarksByHr")
//	private String reMarksByHr;
	
//	@Column(name = "reMarksByManager")
//	private String reMarksByManager;
	
//	@Column(name = "profileScreenRemarks")
//	private String profileScreenRemarks;
	
	@Column(name = "work_Exp",nullable = false)
	private String workExp;
	
//	@Column(name = "client_Round")
//	private String clientRound;
	
	@PrePersist
	protected void onCreate() {
	    creationDate = new Date();
	}
}

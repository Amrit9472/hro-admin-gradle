package com.eos.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "employee_status_details")
public class EmployeeStatusDetails {

	
	    @Id 
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    @JoinColumn(name = "employee_id", nullable = false)
	    private Employee employee;

	    @Column(name = "initial_status")
	    private String initialStatus;

	    @Column(name = "processes_status")
	    private String processesStatus;

	    @Column(name = "hr_status")
	    private String hrStatus;

	    @Column(name = "manager_status")
	    private String managerStatus;

	    @Column(name = "last_interview_assign")
	    private String lastInterviewAssign;

	    @Column(name = "remarks_by_hr")
	    private String remarksByHr;

	    @Column(name = "remarks_by_manager")
	    private String remarksByManager;

	    @Column(name = "profile_screen_remarks")
	    private String profileScreenRemarks;

	    @Column(name = "client_round")
	    private String clientRound;
	    
	    @Column(name = "grade")
	    private String grade;
	    
	    @Column(name = "companyType")
	    private String companyType;
	    
//	    @Column(name = "designation")
//	    private String designation;
	    
	    @Column(name = "department")
	    private String department;
	    
}

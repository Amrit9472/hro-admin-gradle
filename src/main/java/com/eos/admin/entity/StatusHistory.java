package com.eos.admin.entity;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StatusHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "employee_id" , nullable = false)
	@JsonBackReference
	private Employee employee; 
	
	@ManyToOne
	@JoinColumn(name = "interview_process_id")
	private InterviewProcesses interviewProcess;
	
	
	@Column(nullable = false)
	private String status;
	
	@Column(name = "hr_name")
	private String hrName;
	
	@Column(name = "remarksOnEveryStages")
	private String remarksOnEveryStages;
	
	@Column(nullable = false)
	private Date changesDateTime;
}

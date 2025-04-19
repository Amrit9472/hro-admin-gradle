package com.eos.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long empId;
	private String notificationMessage;
	private boolean isRead;
	public Notification(Long empId, String notificationMessage, boolean isRead) {
		super();
		this.empId = empId;
		this.notificationMessage = notificationMessage;
		this.isRead = isRead;
	}
	
	

}

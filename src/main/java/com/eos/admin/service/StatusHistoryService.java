package com.eos.admin.service;

import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.entity.Employee;

public interface StatusHistoryService {

	default void createInitialStatus(Employee employee) {
	}

	public void trackStatusChange(Employee employee, String newStatus);
	public void trackStatusChange(StatusRequestDTO statusRequestDTO, Long employeeId);

}

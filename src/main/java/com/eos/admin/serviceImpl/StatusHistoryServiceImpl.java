package com.eos.admin.serviceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.entity.Employee;
import com.eos.admin.entity.StatusHistory;
import com.eos.admin.exception.ResourceNotFoundException;
import com.eos.admin.repository.EmployeeRepository;
import com.eos.admin.repository.StatusHistoryRepository;
import com.eos.admin.service.StatusHistoryService;

@Service
public class StatusHistoryServiceImpl implements StatusHistoryService {

	@Autowired
	private StatusHistoryRepository statusHistoryRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public void createInitialStatus(Employee employee) {
		StatusHistory initialStatus = new StatusHistory();
//		initialStatus.setId(generateUniqueId());
		initialStatus.setEmployee(employee);
		initialStatus.setStatus("Active");
		initialStatus.setChangesDateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		statusHistoryRepository.save(initialStatus);
	}

	private Long generateUniqueId() {
		return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	@Override
	public void trackStatusChange(Employee employee, String newStatus) {
		StatusHistory latestStatusHistory = statusHistoryRepository
				.findTopByEmployeeOrderByChangesDateTimeDesc(employee);
		if (latestStatusHistory != null && !latestStatusHistory.getStatus().equals(newStatus)) {
			StatusHistory statusHistory = new StatusHistory();
			statusHistory.setEmployee(employee);
			statusHistory.setStatus(newStatus);
			statusHistory.setRemarksOnEveryStages(newStatus);
			statusHistory.setChangesDateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			statusHistoryRepository.save(statusHistory);
			employeeRepository.save(employee);

		}
	}

	@Override
	public void trackStatusChange(StatusRequestDTO statusRequestDTO, Long employeeId) {
		StatusHistory req = new StatusHistory();
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		req.setStatus(statusRequestDTO.getNewStatus());
		req.setHrName(statusRequestDTO.getResponseSubmitbyName());
		req.setRemarksOnEveryStages(statusRequestDTO.getRemarks());
		req.setChangesDateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		req.setEmployee(employee);
		statusHistoryRepository.save(req);
	}

}

package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.Employee;
import com.eos.admin.entity.StatusHistory;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
	StatusHistory findTopByEmployeeOrderByChangesDateTimeDesc(Employee employee);
}

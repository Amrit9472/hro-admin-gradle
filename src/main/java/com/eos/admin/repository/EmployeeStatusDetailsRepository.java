package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.Employee;
import com.eos.admin.entity.EmployeeStatusDetails;

public interface EmployeeStatusDetailsRepository extends JpaRepository<EmployeeStatusDetails, Long> {

	EmployeeStatusDetails findByEmployee(Employee savedEmployeeEntity);

}

package com.eos.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByEmail(String email);

	boolean existsByAadhaarNumber(String aadhaarNumber);

	@Query("SELECT e.id, e.fullName, e.email, e.jobProfile, e.mobileNo, e.permanentAddress, e.gender, e.creationDate "
			+ "FROM Employee e INNER JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id "
			+ "WHERE esd.initialStatus = 'Active' AND esd.hrStatus IS NULL")
	List<Object[]> getListOfEmployeeOnProfileScreening();

	@Query("SELECT e.id, e.fullName, e.aadhaarNumber, e.email, e.creationDate, sh.status, sh.changesDateTime ,sh.hrName,sh.remarksOnEveryStages "
			+ "FROM Employee e " + "JOIN StatusHistory sh ON e.id = sh.employee.id " + "WHERE e.id = :id")
	List<Object[]> getListOfStatusHistoryRecordsOfEmployee(@Param("id") Long employeeId);

	@Query("SELECT e.id, e.fullName, e.email, e.gender, e.mobileNo, e.creationDate "
			+ "FROM Employee e JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id "
			+ "WHERE esd.hrStatus = 'Select' AND esd.processesStatus IS NULL")
	List<Object[]> getListOfEmployeeSechedulePage();

}

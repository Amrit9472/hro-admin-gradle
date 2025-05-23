package com.eos.admin.repository;

import java.util.Date;
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

	@Query("SELECT e.id , e.fullName,e.email, e.jobProfile,creationDate  "
			+ "FROM Employee e JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id "
			+ "WHERE esd.processesStatus = 'Reject'")
	List<Object[]> getListOfEmployeeRejectByProcessManager();

	@Query(value = "SELECT e.id,e.fullName,e.email,e.mobileNo,e.gender,e.creationDate,esd.profileScreenRemarks,esd.remarksByHr,esd.remarksByManager "
			+ "FROM Employee e " + "JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id "
			+ "WHERE esd.managerStatus = 'Select'")
	List<Object[]> getSelectedEmployeeDetails();

	@Query("SELECT e.id, e.fullName, e.email, e.gender, e.mobileNo, e.creationDate "
			+ "FROM Employee e JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id "
			+ "WHERE esd.hrStatus = 'Reject'")
	List<Object[]> getListOfProfileScreaningPage();

//	@Query("SELECT e.id ,e.fullName, e.email , esd.initialStatus ,esd.creationDate ,esd.hrStatus ,esd.managerStatus "
//			+" esd.managerStatus, esd.lastInterviewAssign,esd.remarksByHr,esd.remarksByManager,esd.profileScreenRemarks "
//			+" FROM Employee e JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id ")
//	List<Object[]> getRegisterEmployeeInformation();

	@Query("SELECT e.id, e.fullName, e.email, " 
	        + "esd.initialStatus, e.creationDate, esd.hrStatus, esd.managerStatus, "
			+ "esd.lastInterviewAssign, esd.remarksByHr, esd.remarksByManager, esd.profileScreenRemarks "
			+ "FROM Employee e JOIN EmployeeStatusDetails esd ON e.id = esd.employee.id")
	List<Object[]> getRegisterEmployeeInformation();

	@Query(value = "SELECT e.id, e.full_Name, e.email, e.job_Profile,e.qualification,e.mobile_no, e.permanent_address,e.current_address, e.gender, e.creation_Date, " 
	        +"e.previous_Organisation,e.dob, e.marital_Status, e.refferal,e.aadhaar_Number,e.languages, e.experience,e.source,e.sub_Source, "
	        +"esd.initial_status, esd.processes_status, esd.hr_status, esd.manager_status,esd.last_interview_assign, "
	        +"esd.profile_screen_remarks, esd.remarks_by_hr,esd.remarks_by_manager,esd.client_round, "
	        +"sh.status,sh.hr_name,sh.remarks_on_every_stages,sh.changes_date_time "
	        +"FROM employees e " 
	        +"INNER JOIN employee_status_details esd ON e.id = esd.employee_id "
	        +"INNER JOIN status_history sh ON e.id = sh.employee_id",
	       nativeQuery = true)
	List<Object[]> getEmployeeDumpData();

	@Query(value = "SELECT s.employee_id, ed.full_name, ed.qualification,ed.aadhaar_number,ed.creation_date, ed.current_address, ed.dob, "
	        + "ed.email, ed.experience, ed.gender, ed.marital_status, ed.mobile_no, ed.permanent_address, ed.previous_organisation, "
			+ "ed.processes_status, ed.refferal,ed.source, ed.sub_source, ed.work_exp, ed.languages,ed.job_profile, esd.initial_status, " 
	        + "esd.hr_status, esd.manager_status, esd.last_interview_assign, esd.remarks_by_hr, esd.remarks_by_manager, esd.profile_screen_remarks, " 
	        +"GROUP_CONCAT(IFNULL(s.hr_name, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS hr_names, " 
	        +"GROUP_CONCAT(IFNULL(s.changes_date_time, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS changes_date_times, "
	        +"GROUP_CONCAT(IFNULL(s.remarks_on_every_stages, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS remarks, " 
	        +"GROUP_CONCAT(IFNULL(s.status, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS status "
	        +"FROM status_history s "
	        +"JOIN employees ed ON s.employee_id = ed.id "
	        +"JOIN Employee_Status_Details esd ON ed.id = esd.employee_id "
	        +"WHERE ed.creation_date BETWEEN :startDate AND :endDate "
	        +"GROUP BY s.employee_id, ed.full_name",
	       nativeQuery = true)
	List<Object[]> getEmployeeSeqDumpData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	

	@Query("SELECT e.id, e.fullName, e.email, e.jobProfile, e.mobileNo, e.creationDate FROM Employee e JOIN e.employeeStatusDetails esd WHERE esd.processesStatus = :uniqueCodeProcess")
	List<Object[]> getScheduleInterviewManagerPage(@Param("uniqueCodeProcess") String uniqueCodeProcess);

}

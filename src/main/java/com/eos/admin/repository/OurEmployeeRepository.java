package com.eos.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.OurEmployees;
@Repository
public interface OurEmployeeRepository extends JpaRepository<OurEmployees, Long> {

	@Query(value = "SELECT * FROM our_employee oe JOIN employees e ON oe.employee_id = e.id WHERE e.applied_location = :location ", nativeQuery = true)
	List<OurEmployees> getAllOurEmployeesByLocation(@Param("location") String location);

}

package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}

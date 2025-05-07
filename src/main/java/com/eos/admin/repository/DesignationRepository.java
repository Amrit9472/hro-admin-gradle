package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

}

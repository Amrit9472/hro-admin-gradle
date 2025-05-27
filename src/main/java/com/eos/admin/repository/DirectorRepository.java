package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
}
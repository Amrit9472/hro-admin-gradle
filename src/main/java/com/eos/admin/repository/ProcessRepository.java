package com.eos.admin.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.ProcessEntity;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Integer> {
}

package com.eos.admin.repository;

import com.eos.admin.entity.TrainingBatch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingBatchRepository extends JpaRepository<TrainingBatch, Long> {
	
    int countByBatchCodeStartingWith(String prefix);

    List<TrainingBatch> findByBatchCodeStartingWith(String prefix);
}

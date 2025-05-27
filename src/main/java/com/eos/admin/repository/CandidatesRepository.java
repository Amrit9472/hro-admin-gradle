package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.CandidatesEntity;
@Repository
public interface CandidatesRepository extends JpaRepository<CandidatesEntity, Long> {

}

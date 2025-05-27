package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.VendorInfo;

@Repository
public interface VendorInfoRepository extends JpaRepository<VendorInfo, Long> {
}
    
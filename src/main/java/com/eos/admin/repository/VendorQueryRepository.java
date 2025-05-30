package com.eos.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.VendorQuery;

public interface VendorQueryRepository extends JpaRepository<VendorQuery, Long> {
	List<VendorQuery> findByVendorEmail(String vendorEmail);
}

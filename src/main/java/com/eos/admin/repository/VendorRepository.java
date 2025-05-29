package com.eos.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByEmail(String email);
}

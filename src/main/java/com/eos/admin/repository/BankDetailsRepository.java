package com.eos.admin.repository;

import com.eos.admin.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

    Optional<BankDetails> findByVendorInfo_Id(Long vendorId);

    boolean existsByVendorInfo_Id(Long vendorId); 

    
    void deleteByVendorInfo_Id(Long vendorId); 
}
package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.VendorQueryDTO;

public interface QueryService {
    void saveQuery(VendorQueryDTO vendorqueryDTO); 
    List<VendorQueryDTO> getQueriesByVendorEmail(String vendorEmail);
}

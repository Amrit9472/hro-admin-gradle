package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.VendorQueryDTO;
import com.eos.admin.dto.VendorQueryInformationDTO;
import com.eos.admin.entity.VendorQuery;
import com.eos.admin.enums.VendorStatusType;

public interface QueryService {
    void saveQuery(VendorQueryDTO vendorqueryDTO); 
    List<VendorQueryDTO> getQueriesByVendorEmail(String vendorEmail);
	List<VendorQueryInformationDTO> getAllVendorQueries();
	VendorQuery updateStatus(Long id, VendorStatusType newStatus, String remark);
}

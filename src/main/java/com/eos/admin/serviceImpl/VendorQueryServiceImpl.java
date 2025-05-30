package com.eos.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.VendorQueryDTO;
import com.eos.admin.entity.VendorQuery;
import com.eos.admin.repository.VendorQueryRepository;
import com.eos.admin.service.QueryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorQueryServiceImpl implements QueryService {

    @Autowired
    private VendorQueryRepository vendorQueryRepository;

    @Override
    public void saveQuery(VendorQueryDTO queryDTO) {
        VendorQuery query = new VendorQuery(queryDTO.getVendorEmail(), queryDTO.getQueryText());
        vendorQueryRepository.save(query);
    }

    @Override
    public List<VendorQueryDTO> getQueriesByVendorEmail(String vendorEmail) {
        List<VendorQuery> vendorQueries = vendorQueryRepository.findByVendorEmail(vendorEmail);

        return vendorQueries.stream()
                            .map(query -> new VendorQueryDTO(query.getId(), query.getVendorEmail(), query.getQueryText()))
                            .collect(Collectors.toList());
    }
}

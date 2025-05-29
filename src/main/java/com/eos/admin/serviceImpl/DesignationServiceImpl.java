package com.eos.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.entity.Designation;
import com.eos.admin.repository.DesignationRepository;
import com.eos.admin.service.DesignationService;
@Service
public class DesignationServiceImpl implements DesignationService {

	
	private DesignationRepository designationRepository;

	@Autowired
	public DesignationServiceImpl(DesignationRepository designationRepository) {
		super();
		this.designationRepository = designationRepository;
	}


	@Override
	public Designation saveDesignation(Designation designation) {
		// TODO Auto-generated method stub
		 return designationRepository.save(designation);
	}

}

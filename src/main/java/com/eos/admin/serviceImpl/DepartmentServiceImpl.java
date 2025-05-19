package com.eos.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.entity.Department;
import com.eos.admin.repository.DepartmentRepository;
import com.eos.admin.repository.LocationRepository;
import com.eos.admin.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	
	private DepartmentRepository departmentRepository;
	private LocationRepository locationRepository;
	
	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository,LocationRepository locationRepository) {
		super();
		this.departmentRepository = departmentRepository;
		 this.locationRepository = locationRepository;
	}


	@Override
	public Department saveDepartment(Department department) {   
		return departmentRepository.save(department);
	}
//	 @Override
//	    public Department saveDepartment(Department department) {
//	        // Check if location is provided and if location exists in the database
//	        if (department.getLocation() != null && department.getLocation().getLocationId() != null) {
//	            // Fetch the location from the database
//	            Location location = locationRepository.findById(department.getLocation().getLocationId())
//	                .orElseThrow(() -> new RuntimeException("Location not found with id: " + department.getLocation().getLocationId()));
//	            
//	            // Set the fetched location to the department
//	            department.setLocation(location);
//	        }
//	        
//	        // Save the department with the location
//	        return departmentRepository.save(department);
//	    }
}

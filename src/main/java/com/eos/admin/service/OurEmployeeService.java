package com.eos.admin.service;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import com.eos.admin.dto.OurEmployeeDTO;

public interface OurEmployeeService {


	ByteArrayResource saveOurEmployee(OurEmployeeDTO ourEmployeeDTO, Long id);

	List<OurEmployeeDTO> getAllLoiGeneratedEmployee(String location);

}

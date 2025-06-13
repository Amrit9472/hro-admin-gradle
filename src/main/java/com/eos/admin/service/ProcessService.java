package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.ProcessDTO;
import com.eos.admin.dto.ProcessDropdownDTO;

public interface ProcessService {

	List<ProcessDTO> saveProcessInformation(List<ProcessDTO> processDTO);

	List<ProcessDropdownDTO> getDropdownList();

	List<ProcessDropdownDTO> getDropdownListRegister();	
	
}

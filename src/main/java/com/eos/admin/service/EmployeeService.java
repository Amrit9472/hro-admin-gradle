package com.eos.admin.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.dto.EmployeeStatusHistroyDTO;
import com.eos.admin.dto.ProfileScreaningResponseDto;
import com.eos.admin.dto.ScheduleInterviewPageRequestDTO;
import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.enums.RemarksType;

public interface EmployeeService {
	public EmployeeDto createEmployee(EmployeeDto employeeDto, MultipartFile file, String path) throws IOException;
    boolean checkDuplicateEmail(String email);
    boolean checkDuplicateAdhaarNo(String aadharNumber);
	public List<ProfileScreaningResponseDto> getListOfEmployeesOnProfileScreanig();
	void updateRemarks(Long employeeId, StatusRequestDTO statusRequestDTO, RemarksType remarksType);
	public List<EmployeeStatusHistroyDTO> getListOfStatusHistoryRecords(Long employeeId);
	public List<ScheduleInterviewPageRequestDTO> getListOfEmployeesOnScheduleInterviewPage();
}

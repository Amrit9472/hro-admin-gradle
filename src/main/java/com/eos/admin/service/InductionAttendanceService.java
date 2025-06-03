package com.eos.admin.service;

import com.eos.admin.dto.InductionAttendanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface InductionAttendanceService {
    List<String> getAllDistinctProcesses();
    List<InductionAttendanceDTO> getEmployeesByDateAndProcess(LocalDate date, String process);
    void saveAttendance(List<InductionAttendanceDTO> attendanceList);
}

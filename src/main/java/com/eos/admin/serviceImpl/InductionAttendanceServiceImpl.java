package com.eos.admin.serviceImpl;

import com.eos.admin.dto.InductionAttendanceDTO;
import com.eos.admin.entity.InductionAttendance;
import com.eos.admin.entity.Employee;
import com.eos.admin.entity.OurEmployees;
import com.eos.admin.entity.TrainingBatch;
import com.eos.admin.enums.AttendanceType;
import com.eos.admin.repository.InductionAttendanceRepository;
import com.eos.admin.repository.EmployeeRepository;
import com.eos.admin.repository.OurEmployeeRepository;
import com.eos.admin.repository.TrainingBatchRepository;
import com.eos.admin.service.InductionAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InductionAttendanceServiceImpl implements InductionAttendanceService {

    @Autowired
    private OurEmployeeRepository ourEmployeeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private TrainingBatchRepository trainingBatchRepository;

    @Autowired
    private InductionAttendanceRepository attendanceRepository;

    @Override
    public List<String> getAllDistinctProcesses() {
        try {
            return ourEmployeeRepository.findDistinctProcesses();
        } catch (Exception e) {
            log.error("Failed to fetch distinct processes", e);
            return Collections.emptyList(); // return safe default
        }
    }

    @Override
    public List<InductionAttendanceDTO> getEmployeesByDateAndProcess(LocalDate date, String process) {
        try {
            List<OurEmployees> filtered = ourEmployeeRepository.findByProcess(process);
            List<InductionAttendanceDTO> response = new ArrayList<>();

            for (OurEmployees oe : filtered) {
                try {
                    // Convert oe.getJoiningDate() (java.util.Date) to LocalDate
                    if (oe.getJoiningDate() != null) {
                        LocalDate joiningDate = new java.sql.Date(oe.getJoiningDate().getTime()).toLocalDate();

                        // Compare with selected date
                        if (joiningDate.isEqual(date)) {
                            Employee emp = oe.getEmployee();
                            InductionAttendanceDTO dto = new InductionAttendanceDTO();
                            dto.setEmployeeId(emp.getId());
                            dto.setName(emp.getFullName());
                            dto.setDate(date);
                            dto.setProcess(process);
                            response.add(dto);
                        }
                    }
                } catch (Exception ex) {
                    log.warn("Failed to process OurEmployeeId {}: {}", oe.getOurEmployeeId(), ex.getMessage());
                }
            }

            return response;
        } catch (Exception e) {
            log.error("Error fetching employees by date {} and process {}: ", date, process, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<InductionAttendanceDTO> getEmployeesByBatch(Long batchId) {
        List<InductionAttendanceDTO> response = new ArrayList<>();

        try {
            Optional<TrainingBatch> optionalBatch = trainingBatchRepository.findById(batchId);
            if (optionalBatch.isPresent()) {
                TrainingBatch batch = optionalBatch.get();
                List<Long> candidateIds = batch.getCandidateIds();

                for (Long empId : candidateIds) {
                    Employee emp = employeeRepository.findById(empId).orElse(null);
                    if (emp != null) {
                        InductionAttendanceDTO dto = new InductionAttendanceDTO();
                        dto.setEmployeeId(emp.getId());
                        dto.setName(emp.getFullName());
                        dto.setDate(batch.getTrainingStartDate());
                        dto.setProcess(batch.getProcess());
                        dto.setType(AttendanceType.TRAINING);
                        response.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch training batch employees for batchId={}", batchId, e);
        }

        return response;
    }
    @Override
    public List<Map<String, Object>> getAllTrainingBatches() {
        List<Map<String, Object>> batches = new ArrayList<>();

        trainingBatchRepository.findAll().forEach(batch -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", batch.getId());
            map.put("batchCode", batch.getBatchCode());
            map.put("trainingStartDate", batch.getTrainingStartDate());
            batches.add(map);
        });

        return batches;
    }


    @Override
    public void saveAttendance(List<InductionAttendanceDTO> attendanceList) {
        try {
            List<InductionAttendance> entities = attendanceList.stream().map(dto -> {
                InductionAttendance a = new InductionAttendance();
                a.setDate(dto.getDate());
                a.setEmployeeId(dto.getEmployeeId());
                a.setProcess(dto.getProcess());
                a.setStatus(dto.getStatus());
                a.setMarker(dto.getMarker());
                a.setType(dto.getType());
                a.setSubmissionDate(LocalDateTime.now());
                return a;
            }).collect(Collectors.toList());

            attendanceRepository.saveAll(entities);
        } catch (Exception e) {
            log.error("Error saving attendance records", e);
            throw new RuntimeException("Failed to save attendance records", e);
        }
    }
}
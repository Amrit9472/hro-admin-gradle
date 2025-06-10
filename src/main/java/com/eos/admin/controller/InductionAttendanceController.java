package com.eos.admin.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.InductionAttendanceDTO;
import com.eos.admin.service.InductionAttendanceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/attendance")
public class InductionAttendanceController {

    @Autowired
    private InductionAttendanceService attendanceService;

    @GetMapping("/processes")
    public ResponseEntity<?> getAllProcesses() {
        try {
            List<String> processes = attendanceService.getAllDistinctProcesses();
            return ResponseEntity.ok(processes);
        } catch (Exception e) {
            log.error("Error fetching processes: ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch processes.");
        }
    }
    
    @GetMapping("/employees-by-batch")
    public ResponseEntity<?> getEmployeesByBatchId(@RequestParam("batchId") Long batchId) {
        try {
            List<InductionAttendanceDTO> employees = attendanceService.getEmployeesByBatch(batchId);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees for batchId {}: ", batchId, e);
            return ResponseEntity.internalServerError().body("Failed to fetch employees by batch.");
        }
    }


    @GetMapping("/employees")
    public ResponseEntity<?> getEmployeesByDateAndProcess(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("process") String process) {
        try {
            List<InductionAttendanceDTO> employees = attendanceService.getEmployeesByDateAndProcess(date, process);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees for date {} and process {}: ", date, process, e);
            return ResponseEntity.internalServerError().body("Failed to fetch employees.");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAttendance(@RequestBody List<InductionAttendanceDTO> attendanceList) {
        try {
            attendanceService.saveAttendance(attendanceList);
            return ResponseEntity.ok("Attendance saved successfully.");
        } catch (Exception e) {
            log.error("Error saving attendance: ", e);
            return ResponseEntity.internalServerError().body("Failed to save attendance.");
        }
    }
    
    @GetMapping("/batches")
    public ResponseEntity<?> getAllTrainingBatches() {
        try {
            return ResponseEntity.ok(attendanceService.getAllTrainingBatches());
        } catch (Exception e) {
            log.error("Error fetching training batches: ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch training batches.");
        }
    }

}

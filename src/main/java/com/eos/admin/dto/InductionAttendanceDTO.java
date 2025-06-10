package com.eos.admin.dto;

import lombok.Data;

import java.time.LocalDate;

import com.eos.admin.enums.AttendanceType;

@Data
public class InductionAttendanceDTO {
    private Long employeeId;
    private String name;
    private LocalDate date;
    private String process;
    private String status; // "Present" or "Absent"
    private String marker; // who marked it
    private AttendanceType type;   // "training" or "induction"
}

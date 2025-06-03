package com.eos.admin.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InductionAttendanceDTO {
    private Long employeeId;
    private String name;
    private LocalDate date;
    private String process;
    private String status; // "Present" or "Absent"
    private String marker; // who marked it
    private String type;   // "training" or "induction"
}

package com.eos.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.eos.admin.enums.AttendanceType;

@Entity
@Table(name = "induction_attendance")
@Data
public class InductionAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String process;

    private Long employeeId;

    private String status; // Present / Absent

    private String marker; // who marked

    @Enumerated(EnumType.STRING)
    private AttendanceType type;  // training / induction

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;
}
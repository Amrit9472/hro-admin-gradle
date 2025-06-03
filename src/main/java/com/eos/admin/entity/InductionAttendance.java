package com.eos.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "InductionAttendance")
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

    private String type; // training / induction

    @Column(name = "submission_date")
    private LocalDate submissionDate;
}

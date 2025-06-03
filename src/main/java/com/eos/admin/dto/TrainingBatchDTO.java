package com.eos.admin.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TrainingBatchDTO {
    private Long batchid;
    private String batchCode;  // new
    private String process;
    private String city;       // optional: to pass from frontend or extract in backend
    private LocalDate trainingStartDate;
    private String faculty;
    private int maxAttempts;
    private int totalMarks;
    private int passingMarks;
    private boolean certificateRequired;
}


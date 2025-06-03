package com.eos.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "training_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchCode;

    private String process;

    private LocalDate trainingStartDate;

    private String faculty;

    private int maxAttempts;

    private int totalMarks;

    private int passingMarks;

    private boolean certificateRequired;
}

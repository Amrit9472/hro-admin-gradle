package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCandidateListDTO {
    private String process;
    private String fullName;
    private LocalDate inductionCompleteDate;
}

package com.eos.admin.service;

import com.eos.admin.dto.TrainingBatchDTO;
import com.eos.admin.dto.TrainingCandidateListDTO;
import com.eos.admin.repository.OurEmployeeRepository;

import java.util.List;

public interface TrainingBatchService {

    List<String> getAllProcesses();

    List<TrainingBatchDTO> getCandidatesByProcess(String process);

    TrainingBatchDTO saveTrainingBatch(TrainingBatchDTO dto);
    
    List<TrainingCandidateListDTO> getEmployeeDetailsByProcess(String process);
    
    String getProcessCodeByName(String process);

    // New service method: get next batch serial number given a prefix
    int getNextBatchSerial(String prefix);
}

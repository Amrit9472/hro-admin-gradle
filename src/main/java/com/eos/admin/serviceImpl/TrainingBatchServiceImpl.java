package com.eos.admin.serviceImpl;

import com.eos.admin.dto.TrainingBatchDTO;
import com.eos.admin.dto.TrainingCandidateListDTO;
import com.eos.admin.entity.TrainingBatch;
import com.eos.admin.repository.OurEmployeeRepository;
import com.eos.admin.repository.TrainingBatchRepository;
import com.eos.admin.repository.UsersRepository;
import com.eos.admin.service.TrainingBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingBatchServiceImpl implements TrainingBatchService {

    private final OurEmployeeRepository ourEmployeeRepository;
    private final TrainingBatchRepository trainingBatchRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<String> getAllProcesses() {
        try {
            return ourEmployeeRepository.findDistinctProcesses();
        } catch (Exception e) {
            log.error("Error fetching all processes", e);
            throw new RuntimeException("Failed to fetch processes");
        }
    }

    @Override
    public List<TrainingBatchDTO> getCandidatesByProcess(String process) {
        try {
            // Placeholder for future implementation
            return null;
        } catch (Exception e) {
            log.error("Error fetching candidates by process: {}", process, e);
            throw new RuntimeException("Failed to fetch candidates");
        }
    }

    @Override
    public TrainingBatchDTO saveTrainingBatch(TrainingBatchDTO dto) {
        try {
            String city = dto.getCity();
            if (city == null || city.length() < 3) {
                throw new IllegalArgumentException("City must be at least 3 characters");
            }
            String cityPrefix = city.substring(0, 3).toUpperCase();

            String process = dto.getProcess();
            String processInitials = process.length() >= 3 ? process.substring(0, 3).toUpperCase() : process.toUpperCase();

            String processCode = usersRepository.findProcessCodeByProcess(process);
            if (processCode == null) {
                log.warn("Process code not found for process: {}", process);
                processCode = "XXX";
            }

            String batchCodePrefix = cityPrefix + processInitials + processCode;

            int count = trainingBatchRepository.countByBatchCodeStartingWith(batchCodePrefix);
            String serial = String.format("%03d", count + 1);

            String batchCode = batchCodePrefix + serial;

            TrainingBatch entity = TrainingBatch.builder()
                    .process(process)
                    .trainingStartDate(dto.getTrainingStartDate())
                    .faculty(dto.getFaculty())
                    .maxAttempts(dto.getMaxAttempts())
                    .totalMarks(dto.getTotalMarks())
                    .passingMarks(dto.getPassingMarks())
                    .certificateRequired(dto.isCertificateRequired())
                    .batchCode(batchCode)
                    .build();

            TrainingBatch saved = trainingBatchRepository.save(entity);

            dto.setBatchid(saved.getId());
            dto.setBatchCode(batchCode);

            return dto;
        } catch (Exception e) {
            log.error("Error saving training batch", e);
            throw new RuntimeException("Failed to save training batch");
        }
    }

    @Override
    public List<TrainingCandidateListDTO> getEmployeeDetailsByProcess(String process) {
        try {
            List<Object[]> rawData = ourEmployeeRepository.findEmployeeDetailsByProcess(process);

            return rawData.stream()
                .map(obj -> {
                    String proc = (String) obj[0];
                    String fullName = (String) obj[1];

                    java.sql.Date sqlDate = (java.sql.Date) obj[2];
                    LocalDate submissionDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    return new TrainingCandidateListDTO(proc, fullName, submissionDate);
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching employee details for process: {}", process, e);
            throw new RuntimeException("Failed to fetch employee details");
        }
    }

    @Override
    public String getProcessCodeByName(String process) {
        try {
            String codeFromDB = usersRepository.findProcessCodeByProcess(process);
            if (codeFromDB != null) {
                return codeFromDB;
            }
            log.warn("Process code not found for process: {}", process);
            return "XXX";
        } catch (Exception e) {
            log.error("Error fetching process code by name: {}", process, e);
            throw new RuntimeException("Failed to fetch process code");
        }
    }

    @Override
    public int getNextBatchSerial(String prefix) {
        try {
            List<TrainingBatch> batches = trainingBatchRepository.findByBatchCodeStartingWith(prefix);

            if (batches.isEmpty()) {
                return 1;
            }

            int maxSerial = batches.stream()
                .mapToInt(batch -> {
                    String batchCode = batch.getBatchCode();
                    String serialStr = batchCode.substring(prefix.length());
                    try {
                        return Integer.parseInt(serialStr);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid serial format in batch code: {}", batchCode);
                        return 0;
                    }
                })
                .max()
                .orElse(0);

            return maxSerial + 1;
        } catch (Exception e) {
            log.error("Error calculating next batch serial for prefix: {}", prefix, e);
            throw new RuntimeException("Failed to calculate next batch serial");
        }
    }
}

package com.eos.admin.serviceImpl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
//import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.dto.EmployeeStatusHistroyDTO;
import com.eos.admin.dto.ProfileScreaningResponseDto;
import com.eos.admin.dto.ScheduleInterviewPageRequestDTO;
import com.eos.admin.dto.StatusHistoryDTO;
import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.entity.Employee;
import com.eos.admin.entity.EmployeeStatusDetails;
import com.eos.admin.entity.InterviewProcesses;
import com.eos.admin.entity.StatusHistory;
import com.eos.admin.enums.RemarksType;
import com.eos.admin.exception.DuplicateRecordException;
import com.eos.admin.exception.ResourceNotFoundException;
import com.eos.admin.repository.EmployeeRepository;
import com.eos.admin.repository.EmployeeStatusDetailsRepository;
import com.eos.admin.repository.InterviewProcessRepository;
import com.eos.admin.repository.StatusHistoryRepository;
import com.eos.admin.service.EmployeeService;
import com.eos.admin.service.FileService;
import com.eos.admin.service.StatusHistoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;
	private FileService fileSercice;
	private StatusHistoryService statusHistoryService;
	private StatusHistoryRepository statusHistoryRepository;
	private NotificationServiceImple notificationServiceImple;
	private ModelMapper modelMapper;
	private EmployeeStatusDetailsRepository employeeStatusDetailsRepository;
	private InterviewProcessRepository interviewProcessRepository;

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, FileService fileSercice,
			StatusHistoryService statusHistoryService, StatusHistoryRepository statusHistoryRepository,
			NotificationServiceImple notificationServiceImple, ModelMapper modelMapper,
			EmployeeStatusDetailsRepository employeeStatusDetailsRepository,InterviewProcessRepository interviewProcessRepository) {
		super();
		this.employeeRepository = employeeRepository;
		this.fileSercice = fileSercice;
		this.statusHistoryService = statusHistoryService;
		this.statusHistoryRepository = statusHistoryRepository;
		this.notificationServiceImple = notificationServiceImple;
		this.modelMapper = modelMapper;
		this.employeeStatusDetailsRepository = employeeStatusDetailsRepository;
		this.interviewProcessRepository = interviewProcessRepository;
	}

	@Override
	public EmployeeDto createEmployee(EmployeeDto employeeDto, MultipartFile file, String path) throws IOException {

		// Check for duplicate email
		if (checkDuplicateEmail(employeeDto.getEmail())) {
			log.info("Starting employee creation process for: {}", employeeDto.getFullName());
			throw new DuplicateRecordException("Email Already Exists");
		}
		// Check for duplicate Aadhaar number
		if (checkDuplicateAdhaarNo(employeeDto.getAadhaarNumber())) {
			log.error("Duplicate Aadhaar number detected for: {}", employeeDto.getAadhaarNumber());
			throw new DuplicateRecordException("Aadhaar Number Exists ");
		}
		// Upload the file
		log.info("Uploading image for Aadhaar number: {}", employeeDto.getAadhaarNumber());
		String fileName = fileSercice.uploadImage(path, file, employeeDto.getAadhaarNumber());
		employeeDto.setAadharFilename(fileName);
		log.info("File uploaded successfully with name: {}", fileName);

		// Format and set the full name
		String formattedName = capitalizeStringAfterSpacing(employeeDto.getFullName());
		employeeDto.setFullName(formattedName);
		log.info("Formatted full name: {}", formattedName);

		// Map DTO to entity and save
		Employee employeeEntity = modelMapper.map(employeeDto, Employee.class);
		log.info("Saving employee entity to the database: {}", employeeEntity);
		Employee savedEmployeeEntity = employeeRepository.save(employeeEntity);
		log.info("Employee entity saved with ID: {}", savedEmployeeEntity.getId());

		// Create initial status and update status
		statusHistoryService.createInitialStatus(savedEmployeeEntity);
		updateEmployeeStatus(savedEmployeeEntity);
		log.info("Initial status and employee status updated for employee ID: {}", savedEmployeeEntity.getId());

		// Create EmployeeStatusDetails if needed
		createEmployeeStatusDetails(savedEmployeeEntity);

		// Notify admin
		notificationServiceImple.notifyAdminNewEmployee(savedEmployeeEntity.getId(), savedEmployeeEntity.getFullName());
		log.info("Admin notified about new employee with ID: {}", savedEmployeeEntity.getId());

		// Return the saved employee as DTO
		EmployeeDto result = modelMapper.map(savedEmployeeEntity, EmployeeDto.class);
		log.info("Employee creation process completed successfully for: {}", result.getFullName());

		return result;
	}

	@Override
	public List<ProfileScreaningResponseDto> getListOfEmployeesOnProfileScreanig() {
		log.info("Entering getListOfEmployeesOnProfileScreaning method.");
		List<ProfileScreaningResponseDto> employees = new ArrayList<>();
		try {
			List<Object[]> response = employeeRepository.getListOfEmployeeOnProfileScreening();
			if (response == null || response.isEmpty()) {
				log.warn("No employee data found for profile screening.");
				return employees;
			}
			log.info("Successfully fetched employee data from the repository.");
			for (Object[] result : response) {
				if (result == null || result.length != 8) {
					log.warn("Invalid data encountered: skipping incomplete record.");
					continue; // Skip incomplete records
				}

				try {
					ProfileScreaningResponseDto employee = new ProfileScreaningResponseDto();
					employee.setId((Long) result[0]);
					employee.setFullName((String) result[1]);
					employee.setEmail((String) result[2]);
					employee.setJobProfile((String) result[3]);
					employee.setMobileNo((Long) result[4]);
					employee.setPermanentAddress((String) result[5]);
					employee.setGender((String) result[6]);
					employee.setCreationDate((LocalDateTime) result[7]);
					employees.add(employee);
				} catch (NullPointerException e) {
					log.error("Null value encountered while processing the employee data: " + e.getMessage());
					continue; // Skip this record and move to the next
				} catch (ClassCastException e) {
					log.error("Data type mismatch while processing employee record: " + e.getMessage());
					continue; // Skip this record and move to the next
				}
			}
			log.info("Successfully processed employee data.");

			// Sorting by creationDate in descending order
			return employees.stream().sorted((e1, e2) -> e2.getCreationDate().compareTo(e1.getCreationDate())) // Sort
																												// in
																												// reverse
																												// order
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("An error occurred while fetching or processing the employee data: ", e);
			throw new RuntimeException("Error fetching employees for profile screening", e);
		}

	}

	@Override
	public void updateRemarks(Long employeeId, StatusRequestDTO statusRequestDTO, RemarksType remarksType) {

		try {
			log.info("Updating remarks for employee ID: {} with remarks type: {}", employeeId, remarksType);

			Employee searchEmployee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new ResourceNotFoundException("Employee is not Present in Records"));
			log.info("Employee found: {}", searchEmployee);

			EmployeeStatusDetails employeeStatusDetails = searchEmployee.getEmployeeStatusDetails();
			switch (remarksType) {
			case PROFILE: {
				// Check if EmployeeStatusDetails already exists
				log.info("Updating profile screen remarks for employee ID: {}", employeeId);

				if (employeeStatusDetails == null) {

					log.info("No existing EmployeeStatusDetails found for employee ID: {}, creating a new one.",
							employeeId);
					employeeStatusDetails = new EmployeeStatusDetails();
					employeeStatusDetails.setEmployee(searchEmployee); // Set the employee reference

				}

				// Update the profile screen remarks
				log.info("Setting profile screen remarks: {} and HR status: {} for employee ID: {}",
						statusRequestDTO.getRemarks(), statusRequestDTO.getNewStatus(), employeeId);

				employeeStatusDetails.setProfileScreenRemarks(statusRequestDTO.getRemarks());
				employeeStatusDetails.setHrStatus(statusRequestDTO.getNewStatus());

				// Save the updated or new EmployeeStatusDetails back to the repository
				employeeStatusDetailsRepository.save(employeeStatusDetails);
				log.info("EmployeeStatusDetails saved for employee ID: {}", employeeId);

				statusHistoryService.trackStatusChange(statusRequestDTO, employeeId);
				log.info("Status change tracked for employee ID: {}", employeeId);
				break; // Exit the switch case

			}
			case SCHEDULE:{
				if(employeeStatusDetails == null) {
					log.info("No existing EmployeeStatusDetails found for employee ID : {}",employeeId);
					employeeStatusDetails = new EmployeeStatusDetails();
					employeeStatusDetails.setEmployee(searchEmployee);
				}
				log.info("Setting Schedule Interview remarks : {} and  status: {} for employee ID: {} " ,statusRequestDTO.getRemarks()
						,statusRequestDTO.getNewStatus(), employeeId);
				employeeStatusDetails.setRemarksByHr(statusRequestDTO.getRemarks());
				employeeStatusDetails.setProcessesStatus(statusRequestDTO.getProcessName());
				employeeStatusDetails.setLastInterviewAssign(statusRequestDTO.getProcessName());
				employeeStatusDetailsRepository.save(employeeStatusDetails);
				
				Employee employee = new Employee();
				employee.setJobProfile(statusRequestDTO.getJobProfile());
				employeeRepository.save(employee);
				
				InterviewProcesses interviewProcesses = new InterviewProcesses();
				interviewProcesses.setEmployee(searchEmployee);
				interviewProcessRepository.save(interviewProcesses);
				statusHistoryService.trackStatusChange(statusRequestDTO, employeeId);
				break;
				
				
			}
			default:
				// Log the case of an unexpected remarks type
				log.error("Unexpected remarks type: {}", remarksType);
				throw new IllegalArgumentException("Unexpected value: " + remarksType);
			}
		} catch (ResourceNotFoundException e) {
			// Log the error if employee is not found
			log.error("Employee not found with ID: {}", employeeId, e);
			throw e; // Re-throw the exception after logging
		} catch (IllegalArgumentException e) {
			// Log the error if there's an invalid remarks type
			log.error("Invalid remarks type for employee ID: {}", employeeId, e);
			throw e; // Re-throw the exception after logging
		} catch (Exception e) {
			// Log any unexpected errors
			log.error("An unexpected error occurred while updating remarks for employee ID: {}", employeeId, e);
			throw new RuntimeException("An unexpected error occurred", e); // Re-throw as a runtime exception
		}

	}

	@Override
	public List<EmployeeStatusHistroyDTO> getListOfStatusHistoryRecords(Long employeeId) {
		log.info("Fetching status history records for employee ID: {}", employeeId);

		try {
			List<Object[]> response = employeeRepository.getListOfStatusHistoryRecordsOfEmployee(employeeId);

			if (response.isEmpty()) {
				log.warn("No status history records found for employee ID: {}", employeeId);
			}

			Map<Long, EmployeeStatusHistroyDTO> employeeMap = new HashMap<>();

			// Iterate through the response and process each row
			for (Object[] row : response) {
				Long employeeIdFromDb = (Long) row[0];

				// If the employee is not already in the map, create a new DTO
				if (!employeeMap.containsKey(employeeIdFromDb)) {
					EmployeeStatusHistroyDTO employeeDto = new EmployeeStatusHistroyDTO();
					employeeDto.setId(employeeIdFromDb);
					employeeDto.setFullName((String) row[1]);
					employeeDto.setAadhaarNumber((String) row[2]);
					employeeDto.setEmail((String) row[3]);
					employeeDto.setCreationDate((Date) row[4]);
					employeeDto.setStatusHistory(new ArrayList<>()); // Initialize status history list
					employeeMap.put(employeeIdFromDb, employeeDto);
					log.debug("Created new EmployeeStatusHistroyDTO for employee ID: {}", employeeIdFromDb);
				}

				// Create a new StatusHistory object from the current row
				StatusHistory statusHistory = new StatusHistory();
				statusHistory.setStatus((String) row[5]);
				statusHistory.setChangesDateTime((Date) row[6]);
				statusHistory.setHrName((String) row[7]);
				statusHistory.setRemarksOnEveryStages((String) row[8]);
				StatusHistoryDTO statusHistoryDTO = modelMapper.map(statusHistory, StatusHistoryDTO.class);

				// Add the status history to the employee's status history list
				employeeMap.get(employeeIdFromDb).getStatusHistory().add(statusHistoryDTO);
				log.debug("Added status history for employee ID: {} with status: {}", employeeIdFromDb,
						statusHistory.getStatus());

			}

			// Return the list of EmployeeStatusHistroyDTO values from the map
			log.info("Successfully processed status history records for employee ID: {}", employeeId);
			return new ArrayList<>(employeeMap.values());
		} catch (Exception e) {
			// Log the exception with detailed information
			log.error("Error occurred while fetching or processing status history records for employee ID {}: {}",
					employeeId, e.getMessage(), e);
			throw new RuntimeException("Error occurred while fetching status history records", e);
		}
	}

	@Override
	public List<ScheduleInterviewPageRequestDTO> getListOfEmployeesOnScheduleInterviewPage() {
		// TODO Auto-generated method stub
		log.info("Request for get list of Employee on schedule Interview page from Empoloyee serviceImp");
		try {
			List<Object[]> repositoryResponse = employeeRepository.getListOfEmployeeSechedulePage();
			List<ScheduleInterviewPageRequestDTO> response = new ArrayList<>();
			for(Object[] repositoryResponses : repositoryResponse) {
				ScheduleInterviewPageRequestDTO scheduleInterviewPageRequestDTO = new ScheduleInterviewPageRequestDTO();
				scheduleInterviewPageRequestDTO.setId((Long) repositoryResponses[0]);
				scheduleInterviewPageRequestDTO.setFullName((String) repositoryResponses[1]);
				scheduleInterviewPageRequestDTO.setEmail((String) repositoryResponses[2] );
				scheduleInterviewPageRequestDTO.setGender((String) repositoryResponses[3]);
				scheduleInterviewPageRequestDTO.setMobileNo((Long) repositoryResponses[4]);
				scheduleInterviewPageRequestDTO.setCreationDate((Date) repositoryResponses[5]);
				response.add(scheduleInterviewPageRequestDTO);
			}
	        log.info("Successfully fetched {} employees for schedule interview page", response.size());
          return response.stream()
        		  .sorted((e1,e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
        		  .collect(Collectors.toList());
		} catch (Exception e) {
			  log.error("Error while fetching employees for schedule interview page: {}", e.getMessage(), e);
		        throw new RuntimeException("Failed to fetch schedule interview page data", e);
		}
	
	}
	

	@Override
	public boolean checkDuplicateEmail(String email) {
		boolean checkEmail = employeeRepository.existsByEmail(email);
		return checkEmail;
	}

	@Override
	public boolean checkDuplicateAdhaarNo(String aadharNumber) {
		boolean checkAdhaarNo = employeeRepository.existsByAadhaarNumber(aadharNumber);
		return checkAdhaarNo;
	}

	private void createEmployeeStatusDetails(Employee savedEmployeeEntity) {
		EmployeeStatusDetails statusDetails = new EmployeeStatusDetails();
		statusDetails.setEmployee(savedEmployeeEntity);
		statusDetails.setInitialStatus("Active");

		// Save EmployeeStatusDetails
		employeeStatusDetailsRepository.save(statusDetails);
		log.info("EmployeeStatusDetails created and associated with employee ID: {}", statusDetails.getId());
	}

	private String capitalizeStringAfterSpacing(String name) {
		if (name == null || name.isEmpty()) {
			return name;
		}
		return Arrays.stream(name.split(" ")).map(
				part -> part.isEmpty() ? part : part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	private void updateEmployeeStatus(Employee savedEmployeeEntity) {
		StatusHistory latestStatus = statusHistoryRepository
				.findTopByEmployeeOrderByChangesDateTimeDesc(savedEmployeeEntity);
		if (latestStatus != null) {
			savedEmployeeEntity.setInitialStatus(latestStatus.getStatus());
			employeeRepository.save(savedEmployeeEntity);
		}
	}

	

}

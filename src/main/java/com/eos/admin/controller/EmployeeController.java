package com.eos.admin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import java.net.URLEncoder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.dto.EmployeeExcelReportDto;
import com.eos.admin.dto.EmployeeExcelReportInSequenceDto;
import com.eos.admin.dto.EmployeeInformationDTO;
import com.eos.admin.dto.EmployeeStatusHistroyDTO;
import com.eos.admin.dto.ManagerPageResponseDTO;
import com.eos.admin.dto.ProfileScreanRejectedDTO;
import com.eos.admin.dto.ProfileScreaningResponseDto;
import com.eos.admin.dto.RejectPageEmployeeDTO;
import com.eos.admin.dto.ScheduleInterviewPageRequestDTO;
import com.eos.admin.dto.SelectedEmployeeDTO;
import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.enums.RemarksType;
import com.eos.admin.exception.InvalidInputException;
import com.eos.admin.exception.ResourceNotFoundException;
import com.eos.admin.service.EmployeeService;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Value("${project.file.upload-dir}")
	private String path;

	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	@PostMapping("/createEmployee")
	public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestPart("employee") EmployeeDto employeeDto,
			@RequestPart("image") MultipartFile image) {
		log.info("Employee request data recived {}", employeeDto);
		log.info("Addhaar file from user is recived {}", image);
		try {
			if (employeeDto == null || image == null || image.isEmpty()) {
				log.warn("Employee request data not recived {} {}", employeeDto, image);
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			log.info("Received image size: {} bytes", image.getSize());
			EmployeeDto saveResponse = employeeService.createEmployee(employeeDto, image, path);
			log.info("Successfully created employee with ID: {}", saveResponse.getId());
			return new ResponseEntity<EmployeeDto>(saveResponse, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error("Error occurred while creating employee: {}", e.getMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/listOfEmpPorfileScreaning")
	public ResponseEntity<List<ProfileScreaningResponseDto>> getListOfEmployeesProfileScreaning() {
		log.info("Request received to get the list of employees for profile screening.");
		try {
			List<ProfileScreaningResponseDto> response = employeeService.getListOfEmployeesOnProfileScreanig();
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error occurred while fetching employees for profile screening: ", e);
			return ResponseEntity.status(500).body(null);
		}

	}

	@PutMapping("/hrResponseSubmitionOnProfilePage/{id}")
	public ResponseEntity<?> updateResponseOnProfileScreaning(@PathVariable("id") Long employeeId,
			@RequestBody() StatusRequestDTO statusRequestDTO) {
		log.info("Update request received on profile screening page for employee ID: {} with status: {}", employeeId,
				statusRequestDTO);

		if (statusRequestDTO.getRemarks() == null || statusRequestDTO.getRemarks().isEmpty()) {
			log.warn("Remarks on profile screaning is empty with employeeId: {}", employeeId);
			throw new InvalidInputException("Remarks should not be blank");

		}
		if (statusRequestDTO.getNewStatus() == null || statusRequestDTO.getNewStatus().isEmpty()) {
			log.warn("New Status on profile screaning is empty with employeeId: {}", employeeId);
			throw new InvalidInputException("Select Action Response");
		}
		try {
			employeeService.updateRemarks(employeeId, statusRequestDTO, RemarksType.PROFILE);
			log.info("Profile remarks and status updated successfully for employee ID: {}", employeeId);
			return new ResponseEntity<>("Profile remarks and status updated successfully.", HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			// Handle the case where the employee is not found
			log.error("Employee not found with ID: {}", employeeId, e);
			return new ResponseEntity<>("Employee not found with ID " + employeeId, HttpStatus.NOT_FOUND);

		} catch (InvalidInputException e) {
			// Handle invalid input (like missing remarks or status)
			log.warn("Invalid input for employee ID: {} - {}", employeeId, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			// Catch all other exceptions (e.g., database issues) and return a 500 internal
			// server error
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/employeeStatusTrack/{employeeId}")
	public ResponseEntity<?> getEmployeeStatusHistroyDetails(@PathVariable("employeeId") Long employeeId) {
		log.info("employee id recived for employee status hisotry method {}", employeeId);
		try {
			List<EmployeeStatusHistroyDTO> employeeStatusHistroyDTO = employeeService
					.getListOfStatusHistoryRecords(employeeId);
			log.info("status history response recived for user {}", employeeStatusHistroyDTO);
			return ResponseEntity.ok(employeeStatusHistroyDTO);
		} catch (Exception e) {
			log.error("Error occurred while fetching employee status history for employee ID {}: {}", employeeId,
					e.getMessage(), e);
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/listOfEmpOnSchedulePage")
	public ResponseEntity<?> getListOfEmployeeOnSchedulePage() {
		try {
			log.info("Request Recive for featch list of employee for schedule Interview Page");
			List<ScheduleInterviewPageRequestDTO> request = employeeService.getListOfEmployeesOnScheduleInterviewPage();
			return ResponseEntity.ok(request);
		} catch (Exception e) {
			log.error("Error occurred while fetching employee schedule interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/submitResponseOnScheduleInterviewPage/{employeeId}")
	public ResponseEntity<?> submitScheduleInterviewResponse(@PathVariable("employeeId") Long employeeId,
			@RequestBody StatusRequestDTO scheduleInterviewOnProcessDTO) {
		log.info("Request recive from user for submit and schedule interview fron id :{} response data {}", employeeId,
				scheduleInterviewOnProcessDTO);
		if (scheduleInterviewOnProcessDTO.getRemarks() == null
				|| scheduleInterviewOnProcessDTO.getRemarks().trim().isEmpty()) {
			throw new InvalidInputException("Remarks is required");
		}
		if (scheduleInterviewOnProcessDTO.getProcessName() == null
				|| scheduleInterviewOnProcessDTO.getProcessName().trim().isEmpty()) {
			throw new InvalidInputException("Process Name is required");

		}
		if (scheduleInterviewOnProcessDTO.getJobProfile() == null
				|| scheduleInterviewOnProcessDTO.getJobProfile().trim().isEmpty()) {
			throw new InvalidInputException("job Profile is require");
		}
		employeeService.updateRemarks(employeeId, scheduleInterviewOnProcessDTO, RemarksType.SCHEDULE);
		return ResponseEntity.ok().build();

	}

	@GetMapping("/rejectByManager")
	public ResponseEntity<?> getListOfEmployeeRejectedByManager() {
		try {
			List<RejectPageEmployeeDTO> request = employeeService.getListOfEmployeeRejectedByManager();
			return ResponseEntity.ok(request);
		} catch (Exception e) {
			log.error("Error occurred while fetching employee  rejected interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/submitResponseOnRejectPage/{employeeId}")
	public ResponseEntity<?> submitResponseOnRejectPage(@PathVariable("employeeId") Long employeeId,
			@RequestBody StatusRequestDTO statusRequestDTO) {
		try {
			if (statusRequestDTO.getProcessName() == null || statusRequestDTO.getProcessName().trim().isEmpty()) {
				throw new InvalidInputException("Select the process");
			}
			employeeService.assignInterviewProcessFromRejectPage(employeeId, statusRequestDTO);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("Error occurred while submit response on employee  rejected interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/selectEmployee")
	public ResponseEntity<?> getListOfEmployeeSelected() {
		try {
			log.info("Fetching list of selected employees...");
			List<SelectedEmployeeDTO> response = employeeService.getAllSelectedInterviewList();
	        log.info("Successfully fetched {} selected employees", response.size());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error occurred while submit response on employee  rejected interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/rejectedbyProfileScreaning")
	public ResponseEntity<?> getListRejectedOnProfileScreanPage() {
		try {
			log.info("Fetching list of rejected on profile Screaning employees...");
		 List<ProfileScreanRejectedDTO> response = employeeService.getListOfProfileScreaningRejected();	
		 log.info("Successfully fetched {} rected  employees", response.size());
		 return ResponseEntity.ok(response);
		}catch (Exception e) {
			log.error("Error occurred while submit response on employee  rejected interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/getInfoOfEmployee")
	public ResponseEntity<?> getEmployeeInformation() {
		
		try {
			List<EmployeeInformationDTO> request = employeeService.getEmployeeInformation();
			return ResponseEntity.ok(request);
		} catch (Exception e) {
			log.error("Error occurred while submit response on employee  rejected interview page ");
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/reportData")
	public ResponseEntity<byte[]> dumpReportData(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws IOException, java.io.IOException {
		List<EmployeeExcelReportDto> response = employeeService.getEmployeesDumpReportData(startDate, endDate);
		ByteArrayOutputStream byteArrayOutputStream = employeeService.exportToRawExcel(response);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=Employee_Report.xlsx");
		return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/export")
	public void  exportToExcel(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, HttpServletResponse response) throws IOException
			 {
		List<Object[]> data = employeeService.getEmployeesDumpReportData(startDate, endDate);
		 // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "Employee_Dump_Report.xlsx";
        String headerValue = "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        // Generate Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Dump");

        String[] headers = {
                "Employee ID", "Full Name", "Qualification", "Aadhaar", "Creation Date", "Current Address",
                "DOB", "Email", "Experience", "Gender", "Marital Status", "Mobile", "Permanent Address",
                "Previous Organisation", "Process Status", "Referral", "Source", "Sub Source", "Work Exp",
                "Languages", "Job Profile", "Initial Status", "HR Status", "Manager Status",
                "Last Interview Assign", "Remarks By HR", "Remarks By Manager", "Profile Screen Remarks",
                "HR Names", "Change Date Times", "Remarks History", "Statuses"
        };

        // Header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data rows
        int rowNum = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                if (rowData[i] != null) {
                    cell.setCellValue(rowData[i].toString());
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
	@GetMapping("/getScheduleInterviewManager/{uniqueCodeProcess}")
	public ResponseEntity<?> getScheduleInterviewManager(@PathVariable("uniqueCodeProcess") String uniqueCodeProcess){
		try {
		List<ManagerPageResponseDTO> request = employeeService.getScheduleInterviewManagerPage(uniqueCodeProcess);
		return ResponseEntity.ok(request);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		
	}
}

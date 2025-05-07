package com.eos.admin.serviceImpl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.OurEmployeeDTO;
import com.eos.admin.entity.Employee;
import com.eos.admin.entity.OurEmployees;
import com.eos.admin.repository.EmployeeRepository;
import com.eos.admin.repository.OurEmployeeRepository;
import com.eos.admin.service.OurEmployeeService;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OurEmployeeServiceImpl implements OurEmployeeService {

	private OurEmployeeRepository ourEmployeeRepository;
	private EmployeeRepository employeeRepository;
	private ModelMapper modelMapper;

	@Autowired
	public OurEmployeeServiceImpl(OurEmployeeRepository ourEmployeeRepository, ModelMapper modelMapper,
			EmployeeRepository employeeRepository) {
		super();
		this.ourEmployeeRepository = ourEmployeeRepository;
		this.modelMapper = modelMapper;
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	@Override
	public ByteArrayResource saveOurEmployee(OurEmployeeDTO ourEmployeeDTO, Long employeeId) {
		// Fetch the Employee entity using the provided employeeId
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

		// Map the DTO to the OurEmployees entity
		OurEmployees newEmployee = modelMapper.map(ourEmployeeDTO, OurEmployees.class);
		newEmployee.setEmployee(employee); // Set the Employee reference

		// Save the new OurEmployees entity
		OurEmployees savedEmployee = ourEmployeeRepository.save(newEmployee);

		try {
			return generatePdf(savedEmployee , employeeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Failed to generate PDF", e);
		}

	}

	@Override
	public List<OurEmployeeDTO> getAllLoiGeneratedEmployee(String location) {
		// TODO Auto-generated method stub
		
		List<OurEmployees> repoResponse = ourEmployeeRepository.getAllOurEmployeesByLocation(location);
		 List<OurEmployeeDTO> dtoList = repoResponse.stream()
			        .map(emp -> modelMapper.map(emp, OurEmployeeDTO.class))
			        .collect(Collectors.toList());
		
		return dtoList;
	}

	private ByteArrayResource generatePdf(OurEmployees savedEmployee ,Long employeeId) throws java.io.IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);

			// Extract data from the savedEmployee and related Employee object
			String name = savedEmployee.getEmployee().getFullName(); // Assuming you have a fullName field
	        Long referenceId = employeeId;   // Assuming there's a referenceId in OurEmployees
			String designation = savedEmployee.getDesignation();
			String department = savedEmployee.getDepartment();
			String processCode = savedEmployee.getProcess();
			Date joiningDate = savedEmployee.getJoiningDate(); 
			Long trainingDays = savedEmployee.getTrainingDays();
			// Assuming it's a String; if LocalDate, format
																// accordingly
			Double ctc = savedEmployee.getCtc(); // Assuming it's a String or format if numeric

			
			PdfFont helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			// Reference and Date
			document.add(new Paragraph("Date: " + LocalDate.now()).setFont(helvetica));
	        document.add(new Paragraph("Reference No: " + referenceId));
	    
	     	document.add(new Paragraph("Letter of Intent").setTextAlignment(TextAlignment.CENTER)
	     					.setFontSize(14).setMarginBottom(20));
	     	
			document.add(new Paragraph("Dear " + name + ",").setMarginTop(10));

			// Letter body
			document.add(new Paragraph("We are happy to announce that you have been selected for the position of \""
					+ designation + "\" for OPERATIONS (" + processCode
					+ ") Department in Eureka Outsourcing Solutions Pvt. Ltd. \"EOS\""));

			document.add(new Paragraph("Your date of joining/induction would not be later than " + joiningDate + "."));

			document.add(new Paragraph("Your total monthly CTC for this position would be Rs. " + ctc + "/-"));

			document.add(new Paragraph(
					"(Detailed salary annexure would be shared along with your appointment letter subject to all relevant tax laws)").setFontSize(10).setMarginTop(0));

			document.add(new Paragraph(
					"We look forward for a long-lasting performance and growth oriented association with you. "
							+ "You are requested to submit the documents listed overleaf on the date of joining for further proceedings. "
							+ "Kindly note that this is a Letter of Intent and your joining would be subject to submission of required documents, "
							+ "verification and training certification. All Original documents required for joining are to be submitted for verification. "
							+ "In case of any irregularity in the Original documents your joining would be put on hold till the final verification."));

			if ("Yes".equalsIgnoreCase(savedEmployee.getTrainingApplicable())) {
 
				document.add(new Paragraph("You will be paid a stipend amount of Rs. 700.0/- during the training period..."));
				
				document.add(new Paragraph(
				        "The training duration for the process would be of " + trainingDays + " days and the stipend will be credited along with your 2nd month salary. "
				        + "In case you do not pass the certification subsequent to the training you will not be eligible for receiving the stipend amount for the appropriate days. "
				        + "During the training period if you do not report to work for 2 consecutive days without intimation you will be treated as absconding. "
				        + "No Stipend would be payable in such case. "));
				document.add(new Paragraph(
				         "Training period can extend by 3-4 working days depending upon the content coverage, and trainees capability in learning the subject matter. "
				        + "The extended period shall form part of the stipend amount stated in the LOI."));

			}
			// Footer
			document.add(new Paragraph("\n\nIssued by"));
			document.add(new Paragraph("Eureka Outsourcing Solutions Pvt. Ltd"));
			document.add(new Paragraph("Human Resource Department"));
			document.add(
					new Paragraph("\nNote: This is a computer generated document. Hence does not require signature."));

			document.close();
			return new ByteArrayResource(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Error generating PDF", e);
		}
	}
}

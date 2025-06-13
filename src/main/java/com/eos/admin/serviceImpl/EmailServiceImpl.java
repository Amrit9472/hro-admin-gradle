package com.eos.admin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.StatusRequestDTO;
import com.eos.admin.entity.EmployeeStatusDetails;
import com.eos.admin.entity.OurEmployees;
import com.eos.admin.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.username}")
	private String fromEmail;

	private final JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;

	}

	@Override
	public void sendRegistrationEmail(String toEmail, String rawPassword) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("Vendor Registration - Confirmation");
			message.setText("Hi,\n\nYou are successfully registered as a vendor.\n\nLogin Details:\nEmail: " + toEmail
					+ "\nPassword: " + rawPassword + "\n\nRegards,\nVendor Team");
			message.setFrom(fromEmail);

			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace(); // Consider logging it instead
		}
	}

	@Override
	public void sendScheduleInterviewEmailToManager(List<String> toEmails) {
		if (toEmails == null || toEmails.isEmpty()) {
			log.warn("No recipient provided for interview schedule email.");
			throw new IllegalArgumentException("Email recipient cannot be null or empty.");
		}

		for (String email : toEmails) {
			if (!isValidEmail(email)) {
				log.error("Invalid email format: {}", email);
				throw new IllegalArgumentException("Invalid email address format: " + email);
			}
		}

		log.info("Preparing to send interview schedule email to: {}", toEmails);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmails.toArray(new String[0]));

			message.setSubject("Interview Schedule Notification");
			message.setText("Dear Manager,\n\n" + "Please be informed that an interview has been scheduled.\n\n"
					+ "Best regards,\nHR Team");
			log.debug("Sending email to: {}", toEmails);
			javaMailSender.send(message);
			log.info("Interview schedule email sent successfully to: {}", toEmails);
		} catch (Exception e) {
			log.error("Failed to send interview schedule email to: {}", toEmails, e);
			throw new RuntimeException("Failed to send email. Reason: " + e.getMessage(), e);
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email != null && email.matches(emailRegex);
	}

	@Override
	public void emailsendEmailWithAttachment(String toEmail, String filename, ByteArrayResource pdfResource) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject("Letter of Intent from EOS");
			helper.setText("Dear Candidate,\n\nPlease find attached your Letter of Intent.\n\nRegards,\nHR Department");

			helper.addAttachment(filename, pdfResource);

			javaMailSender.send(message);
			log.info("Email with PDF attachment sent successfully to {}", toEmail);
		} catch (Exception e) {
			log.error("Failed to send email with attachment to {}: {}", toEmail, e.getMessage(), e);
			throw new RuntimeException("Failed to send email with attachment", e);
		}
	}

	@Override
	public void emailsendEmailWithAttachment(String toEmail, String filename, ByteArrayResource pdfResource,
			OurEmployees savedEmployee) {
		try {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(fromEmail);
	        helper.setTo(toEmail);
	        helper.setSubject("Letter of Intent from EOS");

	        String emailContent = String.format(
	            "Dear %s,\n\n" +
	            "We are happy to announce that you have been selected for the position of %s for %s Department in Eureka Outsourcing Solutions Pvt. Ltd.\n\n" +
	            "Enclosed is your Letter of Intent for your reference.\n\n" +
	            "Reference No - %s\n\n" +
	            "This Letter of Intent (LOI) will be valid for 7 days from the date of this email. " +
	            "If this LOI is acceptable to you, please let us know your acceptance of employment by confirming via email within 7 days of receiving this email. " +
	            "If we do not hear back from you within this period, this offer will be deemed to be cancelled & we are not obliged to hold the position open for you.\n\n" +
	            "Thanks,\n\n" +
	            "Eureka Outsourcing Solutions Pvt. Ltd.\n" +
	            "Human Resource Department",
	            savedEmployee.getEmployee().getFullName(),
	            savedEmployee.getEmployee().getJobProfile(),
	            savedEmployee.getDepartment(),
	            savedEmployee.getEmployee().getId()
	        );

	        helper.setText(emailContent);
	        helper.addAttachment(filename, pdfResource);
	        javaMailSender.send(message);
	        log.info("Email with PDF attachment sent successfully to {}", toEmail);
	    } catch (Exception e) {
	        log.error("Failed to send email with attachment to {}: {}", toEmail, e.getMessage(), e);
	        throw new RuntimeException("Failed to send email with attachment", e);
	    }		
	}

	@Override
	public void sendScheduleInterviewEmailToManager(List<String> emailList,
			EmployeeStatusDetails employeeStatusDetails ,StatusRequestDTO statusRequestDTO) {

		  if (emailList == null || emailList.isEmpty()) {
		        log.warn("No recipient provided for interview schedule email.");
		        throw new IllegalArgumentException("Email recipient cannot be null or empty.");
		    }

		    for (String email : emailList) {
		        if (!isValidEmail(email)) {
		            log.error("Invalid email format: {}", email);
		            throw new IllegalArgumentException("Invalid email address format: " + email);
		        }
		    }

		    log.info("Preparing to send interview schedule email to: {}", emailList);

		    try {
		        MimeMessage message = javaMailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true);

		        helper.setFrom(fromEmail);
		        helper.setTo(emailList.toArray(new String[0]));
		        helper.setSubject("Interview Scheduled – Candidate Details for Your Action");

		        String htmlContent = "<p>Dear Manager,</p>"
		                + "<p>We have scheduled an interview and request your support in completing the process as planned. Please find the candidate details below:</p>"
		                + "<p><strong>Candidate Name:</strong> " + employeeStatusDetails.getEmployee().getFullName() + "<br>"
		                + "<strong>Position Applied For:</strong> " + statusRequestDTO.getJobProfile() + "<br>"
		                + "<strong>Process Name:</strong> " + statusRequestDTO.getProcessName() + "</p>"
		                + "<p>Kindly ensure the interview is conducted as per the schedule.</p>"
		                + "<p>Click the link below to check more details:</p>"
		                + "<p><a href='http://localhost:5173/' target='_blank'>Click here to check</a></p>"
		                + "<p>Thank you for your support.<br><br>Best regards,<br>HR Team</p>";

		        helper.setText(htmlContent, true);  // `true` to indicate HTML

		        log.debug("Sending HTML email to: {}", emailList);
		        javaMailSender.send(message);
		        log.info("Interview schedule email sent successfully to: {}", emailList);

		    } catch (Exception e) {
		        log.error("Failed to send interview schedule email to: {}", emailList, e);
		        throw new RuntimeException("Failed to send email. Reason: " + e.getMessage(), e);
		    }
	}
}

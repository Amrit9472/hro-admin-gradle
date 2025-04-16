package com.eos.admin.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExcelReportInSequenceDto {
	private Long id;
    private String fullName;
    private String qualification;
    private String aadhaarNumber;
    private Date creationDate;
    private String currentAddress;
    private Date dob;
    private String email;
    private Float experience;
    private String gender;
    private String hrStatus;
    private String initialStatus;
    private String jobProfile;
    private String languages;
    private String lastInterviewAssin;
    private String managerStatus;
    private String maritalStatus;
    private Long mobileNo;
    private String permanentAddress;
    private String previousOrganisation;
    private String processesStatus;
    private String profileScreenRemarks;
    private String reMarksByHr;
    private String reMarksByManager;
    private String refferal;
    private String source;
    private String subSource;
    private String workExp;
    private List<StatusHistoryExcelDto> statusHistory;
    
    
    public void addStatusHistory(StatusHistoryExcelDto statusHistoryDto) {
        this.statusHistory.add(statusHistoryDto);
    }
}

package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedFormDTO {

    // VENDOR DETAILS
    private Long id;
    private String companyName;
    private String address;
    private String city;
    private String pinCode;
    private String telephone;
    private String mobile;
    private String email;
    private String contactPerson;
    private String pan;
    private String gst;
    private String msme;
    private String serviceType;
    private String serviceTypeOther;
    private boolean declaration;

    // RELATED CHILD TABLES
    private List<DirectorDTO> directors;
    private BankDetailsDTO bankDetails;
}
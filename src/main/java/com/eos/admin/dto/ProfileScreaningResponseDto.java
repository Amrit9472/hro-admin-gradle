package com.eos.admin.dto;



import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileScreaningResponseDto {
	private Long id;
    private String fullName;
    private String email;
    private Long mobileNo;
    private String jobProfile;
    private String permanentAddress;
    private String gender;
    private Date creationDate;
    private List<String> languages;

}

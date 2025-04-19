package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInterviewPageRequestDTO {
  private Long id;
  private String fullName;
  private String email;
  private Long mobileNo;
  private String gender;
  private Date creationDate;
  
  
}

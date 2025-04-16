package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInformationDTO {
      private Long id;
      private String fullName;
      private String email;
      private String initialStatus;
      private Date creationDate;
      private String hrStatus;
      private String managerStatus;
      private String lastInterviewAssign;
      private String remarksByHr;
      private String remarksByManager;
      private String profileScreenRemarks;
}

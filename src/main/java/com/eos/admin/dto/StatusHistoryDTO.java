package com.eos.admin.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryDTO {
	    private String status;
	    private Date changesDateTime;
	    private String hrName;
	    private String remarksOnEveryStages;
}

package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryExcelDto {

	 private String hrName;
	 private String changesDateTime;
	 private String remarks;
	 private String status;
}

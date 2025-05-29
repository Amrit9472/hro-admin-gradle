package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoiInfoDropDownDTO {

	private String process;
	private String grade;
	private String companyType;
	private String grid;

}

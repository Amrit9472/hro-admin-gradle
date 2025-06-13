package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProcessDropdownDTO {
    private String name;
    private Long code;
	public ProcessDropdownDTO(String name) {
		super();
		this.name = name;
	}

    
}

package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO {
    private Long id;
    private String name;
    private String designation;
    private String address;
    private String phone;
}
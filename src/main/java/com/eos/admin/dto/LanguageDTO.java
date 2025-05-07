package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LanguageDTO {

    private String languageName;
    private boolean canRead;
    private boolean canWrite;

    
}

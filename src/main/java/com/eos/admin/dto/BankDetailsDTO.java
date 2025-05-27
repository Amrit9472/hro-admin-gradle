package com.eos.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDTO {
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String ifscCode;
    private String branchAddress;
    private String chequeImagePath;
}
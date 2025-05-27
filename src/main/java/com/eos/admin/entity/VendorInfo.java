package com.eos.admin.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendor_info")
public class VendorInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String address;
    private String city;
    private String pinCode;
    private String telephone;
    private String mobile;
    private String email;
    private String contactPerson;

    private String pan;
    private String gst;
    private String msme;

    private String serviceType;
    private String serviceTypeOther;

    private boolean declaration;

    @OneToMany(mappedBy = "vendorInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Director> directors;

    @OneToOne(mappedBy = "vendorInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private BankDetails bankDetails;
}
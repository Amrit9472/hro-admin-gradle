package com.eos.admin.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"candiEmail", "scheme"})
)
public class CandidatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String candiName;
    private String candiMobile;
    private String candiEmail;
    
    private LocalDateTime submittedDate;
    
    @PrePersist
    protected void onCreate() {
        submittedDate = LocalDateTime.now();
    }
    
    @Column(name = "vendor_email")
    private String vendorEmail;
    
    @Column(name = "scheme")
    private String scheme;
}

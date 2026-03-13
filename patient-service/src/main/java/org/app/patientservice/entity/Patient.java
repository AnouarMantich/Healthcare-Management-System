package org.app.patientservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Patient {

    @Id
    @GeneratedValue
    private int id;

    private String bloodType;

    private String medicalHistory;

    private String insuranceNumber;

}

package org.app.patientservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.app.patientservice.enums.BloodType;

import java.util.List;


@Entity
@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class Patient {

    @Id
    private String id;

//    String medicalRecordNumber;
    @Enumerated(EnumType.STRING)
   private BloodType bloodType;
   private List<String> allergies;
   private List<String> chronicDiseases;
   private String insuranceNumber;
   private String emergencyContact;

}

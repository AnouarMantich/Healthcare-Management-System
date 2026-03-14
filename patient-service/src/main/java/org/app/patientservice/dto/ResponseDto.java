package org.app.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.app.patientservice.enums.BloodType;

import java.util.List;


@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class ResponseDto {


    private String id;

    //    String medicalRecordNumber;
    private BloodType bloodType;
    private List<String> allergies;
    private List<String> chronicDiseases;
    private String insuranceNumber;
    private String emergencyContact;

}

package org.app.patientservice.mapper;


import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.entity.Patient;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class PatientMapper {


    public static ResponseDto  toResponseDto(Patient patient) {
        ResponseDto responseDto = new ResponseDto();
        BeanUtils.copyProperties(patient, responseDto);
        return responseDto;
    }


    public static Patient toPatient(RequestDto requestDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(requestDto, patient);
//        for now we will add an arbitrary id , by we should get the user id from user service and set it here
        patient.setId(UUID.randomUUID().toString());
        return patient;

    }


    public static Patient updatePatient (Patient patient,RequestDto requestDto) {


        if (requestDto == null) return patient;

        if (requestDto.getAllergies() != null ) {
            patient.setAllergies(requestDto.getAllergies());
        }
        if (requestDto.getBloodType() != null ) {
            patient.setBloodType(requestDto.getBloodType());
        }

        if (requestDto.getChronicDiseases() != null) {
            patient.setChronicDiseases(requestDto.getChronicDiseases());
        }
        if (requestDto.getEmergencyContact() != null && !requestDto.getEmergencyContact().isEmpty()) {
            patient.setEmergencyContact(requestDto.getEmergencyContact());
        }

        if (requestDto.getInsuranceNumber() != null && !requestDto.getInsuranceNumber().isEmpty()) {
            patient.setInsuranceNumber(requestDto.getInsuranceNumber());
        }

        return patient;

    }


}

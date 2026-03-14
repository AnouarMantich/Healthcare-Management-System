package org.app.patientservice.mapper;


import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.entity.Patient;
import org.springframework.beans.BeanUtils;

public class PatientMapper {


    public static ResponseDto  toResponseDto(Patient patient) {
        ResponseDto responseDto = new ResponseDto();
        BeanUtils.copyProperties(patient, responseDto);
        return responseDto;
    }


    public static Patient toPatient(ResponseDto responseDto) {

        Patient patient = new Patient();
        BeanUtils.copyProperties(responseDto, patient);
        return patient;

    }


}

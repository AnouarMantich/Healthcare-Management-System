package org.app.patientservice.service;

import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;

import java.util.List;

public interface PatientService {

    List<ResponseDto> getPatients();
    ResponseDto getPatientById(String id);
    ResponseDto addPatient(RequestDto requestDto);
    ResponseDto updatePatient(RequestDto requestDto);
    ResponseDto deletePatient(String id);


}

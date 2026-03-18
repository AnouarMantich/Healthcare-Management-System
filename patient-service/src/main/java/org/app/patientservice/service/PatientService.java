package org.app.patientservice.service;

import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.exception.UserNotPatientException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PatientService {

    List<ResponseDto> getPatients(int page, int size, Sort.Direction direction, String sortBy);
    ResponseDto getPatientById(String id);

    ResponseDto getPatientByCin(String cin) throws UserNotPatientException;

    ResponseDto addPatient(String id, RequestDto requestDto) throws UserNotPatientException;
    ResponseDto updatePatient(String id,RequestDto requestDto);
    void deletePatient(String id);


}

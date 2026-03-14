package org.app.patientservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    @Override
    public List<ResponseDto> getPatients() {
        return List.of();
    }

    @Override
    public ResponseDto getPatientById(String id) {
        return null;
    }

    @Override
    public ResponseDto addPatient(RequestDto requestDto) {
        return null;
    }

    @Override
    public ResponseDto updatePatient(RequestDto requestDto) {
        return null;
    }

    @Override
    public ResponseDto deletePatient(String id) {
        return null;
    }
}

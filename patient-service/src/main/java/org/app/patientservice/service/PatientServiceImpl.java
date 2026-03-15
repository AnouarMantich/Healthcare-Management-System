package org.app.patientservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.entity.Patient;
import org.app.patientservice.exception.PatientNotFoundException;
import org.app.patientservice.mapper.PatientMapper;
import org.app.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Override
    public List<ResponseDto> getPatients() {

        List<Patient> allPatients = patientRepository.findAll();

        return allPatients.stream().map(PatientMapper::toResponseDto).toList();
    }

    @Override
    public ResponseDto getPatientById(String id) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        return PatientMapper.toResponseDto(patient);
    }

    @Override
    public ResponseDto addPatient(RequestDto requestDto) {

        Patient patient = PatientMapper.toPatient(requestDto);
        Patient savedPatient = patientRepository.save(patient);

        return PatientMapper.toResponseDto(savedPatient);
    }

    @Override
    public ResponseDto updatePatient(String id,RequestDto requestDto) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        Patient patientUpdated = PatientMapper.updatePatient(patient, requestDto);

        patientRepository.save(patientUpdated);


        return PatientMapper.toResponseDto(patientUpdated);
    }

    @Override
    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(patient);
    }
}

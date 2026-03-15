package org.app.patientservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.patientservice.client.UserClient;
import org.app.patientservice.client.dto.UserResponse;
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

    private final PatientRepository patientRepository;
    private final UserClient userClient;

    @Override
    public List<ResponseDto> getPatients() {

        List<Patient> allPatients = patientRepository.findAll();

        return allPatients.stream().map(PatientMapper::toResponseDto)
                .peek((patientDTO)->{
                    UserResponse userById = userClient.getUserById(patientDTO.getId());
                    log.warn("this is the user infos {}",userById.toString());

                    if(userById==null){
                        throw new PatientNotFoundException(patientDTO.getId());
                    }
                    patientDTO.setUser(userById);
                })
                .toList();
    }

    @Override
    public ResponseDto getPatientById(String id) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        UserResponse userById = userClient.getUserById(id);
        log.warn("this is the user infos {}",userById.toString());

        if(userById==null){
            throw new PatientNotFoundException(id);
        }

        ResponseDto responseDto = PatientMapper.toResponseDto(patient);
        responseDto.setUser(userById);

        return responseDto;
    }

    @Override
    public ResponseDto addPatient(String id,RequestDto requestDto) {
        UserResponse userById = userClient.getUserById(id);

        log.warn("this is the user infos {}",userById.toString());
        if(userById==null || !userById.isProfileCompleted()){
            throw new PatientNotFoundException(id);
        }

        Patient patient = PatientMapper.toPatient(requestDto);
        patient.setId(id);
        Patient savedPatient = patientRepository.save(patient);
        ResponseDto responseDto= PatientMapper.toResponseDto(savedPatient);
        responseDto.setUser(userById);

        return responseDto;

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

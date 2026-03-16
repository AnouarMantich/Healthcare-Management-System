package org.app.patientservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.patientservice.client.UserClient;
import org.app.patientservice.client.dto.Role;
import org.app.patientservice.client.dto.UserResponse;
import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.entity.Patient;
import org.app.patientservice.exception.PatientNotFoundException;
import org.app.patientservice.exception.UserNotPatientException;
import org.app.patientservice.mapper.PatientMapper;
import org.app.patientservice.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserClient userClient;

    @Override
    public List<ResponseDto> getPatients(int page, int size,Sort.Direction direction, String sortBy) {

        PageRequest pageRequest = PageRequest.of(0, 10, direction, sortBy);

        Page<Patient> all = patientRepository.findAll(pageRequest);


        return all.stream().map(PatientMapper::toResponseDto)
                .peek((patientDTO)->{
                    UserResponse userById = userClient.getUserById(patientDTO.getId());
                    patientDTO.setUser(userById);
                })
                .toList();
    }

    @Override
    public ResponseDto getPatientById(String id) {

        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        UserResponse userById = userClient.getUserById(id);

        ResponseDto responseDto = PatientMapper.toResponseDto(patient);
        responseDto.setUser(userById);

        return responseDto;
    }

    @Override
    public ResponseDto addPatient(String id,RequestDto requestDto) throws UserNotPatientException {
        UserResponse userById = userClient.getUserById(id);
        if (userById.getRole().equals(Role.ADMIN) ||userById.getRole().equals(Role.DOCTOR)){
            throw new UserNotPatientException(id);
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

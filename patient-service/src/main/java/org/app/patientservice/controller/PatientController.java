package org.app.patientservice.controller;


import lombok.AllArgsConstructor;
import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.exception.UserNotPatientException;
import org.app.patientservice.service.PatientService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

    private PatientService patientService;


    @GetMapping()
    public ResponseEntity<List<ResponseDto>> getAllPatients(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "5") Integer size,
            @RequestParam(required = false,defaultValue = "id") String sortField,
            @RequestParam(required = false,defaultValue = "ASC") Sort.Direction direction


    ) {
        return ResponseEntity.ok(patientService.getPatients(page,size,direction,sortField));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }


    @PostMapping("/{id}")
    public ResponseEntity<ResponseDto> addPatient(@PathVariable String id,@RequestBody RequestDto requestDto) throws UserNotPatientException {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.addPatient(id,requestDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updatePatient(@PathVariable String id,@RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(patientService.updatePatient(id,requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();

    }








}

package org.app.patientservice.controller;


import lombok.AllArgsConstructor;
import org.app.patientservice.dto.RequestDto;
import org.app.patientservice.dto.ResponseDto;
import org.app.patientservice.service.PatientService;
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
    public ResponseEntity<List<ResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }


    @PostMapping()
    public ResponseEntity<ResponseDto> addPatient(@RequestBody RequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.addPatient(requestDto));
    }


    @PutMapping()
    public ResponseEntity<ResponseDto> updatePatient(@RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(patientService.updatePatient(requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();

    }








}

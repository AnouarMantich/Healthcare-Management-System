package org.app.patientservice.exception;

import jakarta.persistence.EntityNotFoundException;

public class PatientNotFoundException extends EntityNotFoundException {

    public PatientNotFoundException(String message) {
        super(message);
    }
}
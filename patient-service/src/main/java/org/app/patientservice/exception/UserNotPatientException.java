package org.app.patientservice.exception;

public class UserNotPatientException extends Exception {

    public UserNotPatientException(String message) {
        super(message);
    }
}
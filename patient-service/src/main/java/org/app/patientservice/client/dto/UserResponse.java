package org.app.patientservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String cin;
    private String username;
    private String email;
    private boolean profileCompleted = false;
    private String firstName;
    private String lastName;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Role role;
    private Instant createdAt;
}

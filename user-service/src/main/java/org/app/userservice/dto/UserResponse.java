package org.app.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.app.userservice.entity.Role;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
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

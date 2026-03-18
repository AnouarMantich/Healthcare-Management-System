package org.app.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{

    @Id
    private UUID id;

    @Column(unique = true)
    private String cin;

    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean profileCompleted = false;

    private String firstName;

    private String lastName;

    private String fullName;

    private String address;

    private String phoneNumber;

    private Role role;

    private  Instant createdAt;

}
package org.app.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.userservice.dto.UserResponse;
import org.app.userservice.dto.UserUpdateDto;
import org.app.userservice.entity.Role;
import org.app.userservice.entity.User;
import org.app.userservice.mapper.UserMapper;
import org.app.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    // ✅ Get or create current authenticated user
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal Jwt jwt) {

        UUID keycloakId = UUID.fromString(jwt.getSubject());
        String firstName = jwt.getClaim("given_name");
        String lastName = jwt.getClaim("family_name");
        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String fullName = jwt.getClaim("name");
        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess.get("roles");
        Role role;
        if (roles.contains("ADMIN")) {
            role = Role.ADMIN;
        } else if (roles.contains("DOCTOR")) {
            role = Role.DOCTOR;
        }
        else  {
            role = Role.PATIENT;
        }
        User user= User.builder()
                .id(keycloakId)
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .fullName(fullName)
                .profileCompleted(false)
                .role(role)
                .build();

        UserResponse savedUser = service.getOrCreateUser(user);

//            if (!user.isProfileCompleted()) {
//                return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
//                        .header("X-Profile-Incomplete", "true")
//                        .body(UserMapper.toResponse(user));
//            }

            return ResponseEntity.ok(savedUser);

    }

    // ✅ Get all users (admin only) with pagination
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {


            Sort sort = direction.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            return ResponseEntity.ok(service.findAll(pageable));

    }

    // ✅ Get user by id (admin only)
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
           return  ResponseEntity.ok(service.findById(id));
    }

    // ✅ Get user by id (admin only)
    @GetMapping("/findBy")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findByCin(@RequestParam String cin) {
        return  ResponseEntity.ok(service.findByCin(cin));
    }

    // ✅ Delete user by id (admin only)
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping()
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal Jwt jwt,@RequestBody UserUpdateDto updateDto) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(service.updateProfile(keycloakId, updateDto));
    }
}

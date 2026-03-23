package org.app.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.userservice.dto.UserResponse;
import org.app.userservice.dto.UserUpdateDto;
import org.app.userservice.entity.User;
import org.app.userservice.service.UserService;
import org.app.userservice.util.ApiResponse;
import org.app.userservice.util.JwtParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    private  Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // ✅ Get or create current authenticated user
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN') or @securityService.canAccessUser(#jwt)")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal Jwt jwt) {

      User user = JwtParser.parseJwt(jwt);

        UserResponse savedUser = service.getOrCreateUser(user);
        ApiResponse<UserResponse> response = ApiResponse
                .success(savedUser,"The current user was successfully added");
            return ResponseEntity.ok(response);

    }




    // ✅ Get all users (admin only) with pagination
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {


            Sort sort = direction.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> allUsers = service.findAll(pageable);

        ApiResponse<Page<UserResponse>> response = ApiResponse
                .success(allUsers,"The current user was successfully added");

        Map<String, Object> paginationMetadata = ApiResponse.createPaginationMetadata(page, size, allUsers.getTotalElements(), allUsers.getTotalPages());
        response.setMetadata(paginationMetadata);


        return ResponseEntity.ok(response);
    }



    // ✅ Get user by id (admin only)
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.canAccessUser(#id)")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable UUID id) {

        UserResponse byId = service.findById(id);
        ApiResponse<UserResponse> response =ApiResponse.success(byId,"User retrieved successfully");

        return  ResponseEntity.ok(response);

    }

    // ✅ Get user by id (admin only)
    @GetMapping("/findBy")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> findByCin(@RequestParam String cin) {

        UserResponse byCin = service.findByCin(cin);
        ApiResponse<UserResponse> response = ApiResponse.success(byCin,"User retrieved successfully");

        return  ResponseEntity.ok(response);
    }

    // ✅ Delete user by id (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable UUID id) {
        service.deleteById(id);
        ApiResponse apiResponse= ApiResponse.success("""
                User deleted successfully""","User deleted successfully");
        return  ResponseEntity.ok(apiResponse);
    }


    @PutMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@AuthenticationPrincipal Jwt jwt,@RequestBody UserUpdateDto updateDto) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        UserResponse userResponse = service.updateProfile(keycloakId, updateDto);
        ApiResponse<UserResponse> response= ApiResponse.success(
                userResponse,"The user was successfully updated"
        );

        return ResponseEntity.ok(response);
    }
}

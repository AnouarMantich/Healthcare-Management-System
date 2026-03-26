package org.app.userservice.controller;


import org.app.userservice.dto.UserResponse;
import org.app.userservice.entity.User;
import org.app.userservice.security.SecurityService;
import org.app.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = true) // Important!
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SecurityService securityService;

    @Test
    @DisplayName("GET /me should return 200 and the user details")
    void me_ShouldReturnUser_WhenAuthenticated() throws Exception {

        UserResponse mockResponse = UserResponse.builder()
                .email("test@example.com")
                .build();

        when(userService.getOrCreateUser(any(User.class)))
                .thenReturn(mockResponse);

        when(securityService.canAccessUser(any(Jwt.class)))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/users/me")
                        .with(jwt().jwt(j -> j
                                .subject(UUID.randomUUID().toString())
                                .claim("email", "test@example.com")
                                .claim("realm_access", Map.of(
                                        "roles", List.of("USER","ADMIN")
                                ))
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(userService).getOrCreateUser(any(User.class));
    }


    @Test
    @DisplayName("GET /users should return paginated users for ADMIN")
    void getAllUsers_ShouldReturnPage_WhenAdmin() throws Exception {

        // Arrange
        UserResponse user1 = UserResponse.builder()
                .email("user1@test.com")
                .build();

        UserResponse user2 = UserResponse.builder()
                .email("user2@test.com")
                .build();

        List<UserResponse> users = List.of(user1, user2);

        Page<UserResponse> page = new PageImpl<>(
                users,
                PageRequest.of(0, 10),
                2
        );

        when(userService.findAll(any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc")
                        .with(jwt().jwt(j -> j
                                .subject(UUID.randomUUID().toString())
                                .claim("realm_access", Map.of(
                                        "roles", List.of("ADMIN") // 🔥 critical
                                ))
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))

                // data content
                .andExpect(jsonPath("$.data.content[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$.data.content[1].email").value("user2@test.com"))

                // pagination metadata
                .andExpect(jsonPath("$.metadata.pagination.page").value(0))
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(10))
                .andExpect(jsonPath("$.metadata.pagination.totalItems").value(2))
                .andExpect(jsonPath("$.metadata.pagination.totalPages").value(1));

        // Verify
        verify(userService, times(1))
                .findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("GET /users/{id} should return user when authorized")
    void findById_ShouldReturnUser_WhenAuthorized() throws Exception {

        // Arrange
        UUID userId = UUID.randomUUID();

        UserResponse mockResponse = UserResponse.builder()
                .email("user@test.com")
                .build();

        when(userService.findById(userId)).thenReturn(mockResponse);
        when(securityService.canAccessUser(userId)).thenReturn(true); // 🔥 critical

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .with(jwt().jwt(j -> j
                                .subject(userId.toString()) // optional but realistic
                                .claim("realm_access", Map.of(
                                        "roles", List.of("ADMIN","DOCTOR")
                                ))
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data.email").value("user@test.com"));

        // Verify
        verify(userService).findById(userId);
    }



    @Test
    @DisplayName("GET /users/findBy should return user when authenticated")
    void findByCin_ShouldReturnUser_WhenAuthenticated() throws Exception {

        // Arrange
        String cin = "AA123456";

        UserResponse mockResponse = UserResponse.builder()
                .email("user@test.com")
                .cin(cin)
                .build();

        when(userService.findByCin(cin)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/findBy")
                        .param("cin", cin)
                        .with(jwt().jwt(j -> j
                                .subject(UUID.randomUUID().toString())
                                .claim("realm_access", Map.of(
                                        "roles", List.of("USER") // even USER works
                                ))
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data.email").value("user@test.com"))
                .andExpect(jsonPath("$.data.cin").value(cin));

        verify(userService).findByCin(cin);
    }

    @Test
    @DisplayName("DELETE /users/{id} should delete user when ADMIN")
    void deleteById_ShouldReturn200_WhenAdmin() throws Exception {

        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteById(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .with(jwt().jwt(j -> j
                                .subject(UUID.randomUUID().toString())
                                .claim("realm_access", Map.of(
                                        "roles", List.of("ADMIN")
                                ))
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService).deleteById(userId);
    }

    @Test
    @DisplayName("DELETE /users/{id} should delete user when DOCTOR")
    void deleteById_ShouldReturn200_WhenDoctor() throws Exception {

        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteById(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .with(jwt().jwt(j -> j
                                .subject(UUID.randomUUID().toString())
                                .claim("realm_access", Map.of(
                                        "roles", List.of("DOCTOR")
                                ))
                        )))
                .andExpect(status().isOk());

        verify(userService).deleteById(userId);
    }


    @Test
    @DisplayName("DELETE /users/{id} should return 401 when no authentication")
    void deleteById_ShouldReturn401_WhenNoJwt() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .with(csrf())) // 🔥 important
                .andExpect(status().isUnauthorized());

        verify(userService, never()).deleteById(any());
    }
}
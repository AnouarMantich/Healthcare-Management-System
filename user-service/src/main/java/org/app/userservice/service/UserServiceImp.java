package org.app.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.userservice.dto.UserResponse;
import org.app.userservice.dto.UserUpdateDto;
import org.app.userservice.entity.User;
import org.app.userservice.exception.UserNotFoundException;
import org.app.userservice.mapper.UserMapper;
import org.app.userservice.repository.UserRepository;
import org.app.userservice.security.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository repo;
    private final SecurityService securityService;

    @Override
    public UserResponse getOrCreateUser(User user) {

        User requestedUser = repo.findById(user.getId()).orElseGet(() -> {
            user.setCreatedAt(Instant.now());
            return repo.save(user);
        });

        return UserMapper.toResponse(requestedUser);
    }

    // --- Admin methods ---
    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return repo.findAll(pageable)
                .map(UserMapper::toResponse);
    }

    @Override
    public UserResponse findById(UUID id) {
        if (!securityService.canAccessUser(id)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }

        return repo.findById(id).map(UserMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserResponse findByCin(String cin) {

        if (!securityService.canAccessByCin(cin)) {
            throw new AccessDeniedException("Not allowed");
        }

        return repo.findByCin(cin).map(UserMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void deleteById(UUID id) {
        if (!repo.existsById(id)) throw new UserNotFoundException("User not found");
        repo.deleteById(id);
    }



    @Override
    public UserResponse updateProfile(UUID keycloakId, UserUpdateDto updateDto) {
        User user = repo.findById(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserMapper.updateEntity(user, updateDto);



        return UserMapper.toResponse(repo.save(user));
    }

}

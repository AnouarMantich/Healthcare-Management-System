package org.app.userservice.security;

import lombok.AllArgsConstructor;
import org.app.userservice.entity.User;
import org.app.userservice.repository.UserRepository;
import org.app.userservice.util.JwtParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("securityService")
@AllArgsConstructor// IMPORTANT: name used in @PreAuthorize
public class SecurityService {
    private final UserRepository userRepository;

    public  boolean canAccessUser(UUID id) {
        return isOwner(id) || hasRole("ROLE_ADMIN") || hasRole("ROLE_DOCTOR");
    }

    public  boolean canAccessUser(Jwt jwt) {
        User user = JwtParser.parseJwt(jwt);
        return isOwner(user.getId()) || hasRole("ROLE_ADMIN") || hasRole("ROLE_DOCTOR");
    }

    public  boolean canAccessByCin(String cin) {
        return hasRole("ROLE_ADMIN")
                || hasRole("ROLE_DOCTOR")
                || isOwnerByCin(cin);
    }

    public boolean isOwnerByCin(String cin) {
        String currentUserId = getCurrentUserId();

        return userRepository.findByCin(cin)
                .map(user -> user.getId().toString().equals(currentUserId))
                .orElse(false);
    }

    public  boolean isOwner(UUID id) {
        String currentUserId = getCurrentUserId();
        return id.toString().equals(currentUserId);
    }

    public  boolean hasRole(String role) {
        Authentication auth = getAuth();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private  String getCurrentUserId() {
        return getAuth().getName();
    }

    private  Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
package org.app.userservice.util;

import org.app.userservice.entity.Role;
import org.app.userservice.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JwtParser {


    public static User parseJwt(Jwt jwt) {


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
        return User.builder()
                .id(keycloakId)
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .fullName(fullName)
                .profileCompleted(false)
                .role(role)
                .build();
    }

}

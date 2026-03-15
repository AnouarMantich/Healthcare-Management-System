package org.app.patientservice.client;


import org.app.patientservice.client.dto.UserResponse;
import org.app.patientservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",configuration = FeignClientConfig.class, url = "http://localhost:9999/user-service/api/v1")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable String id);

}

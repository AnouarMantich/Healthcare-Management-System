package org.app.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    @NotBlank(message = "CIN is required")
    @Size(max = 10, message = "CIN should be like xxxxxxxxxx")
    private String cin;

    @NotBlank(message = "Phone number is required")
    @Size(max = 50, message = "Phone number must be less than 50 characters")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;



}
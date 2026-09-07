package com.example.adplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SystemUserCreateRequest {

    @NotBlank(message = "username must not be blank")
    @Size(max = 64, message = "username length must be <= 64")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 6, max = 128, message = "password length must be between 6 and 128")
    private String password;

    @Size(max = 64, message = "nickname length must be <= 64")
    private String nickname;
}

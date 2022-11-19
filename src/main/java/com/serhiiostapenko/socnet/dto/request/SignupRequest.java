package com.serhiiostapenko.socnet.dto.request;

import com.serhiiostapenko.socnet.annotations.PasswordMatches;
import com.serhiiostapenko.socnet.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @NotEmpty(message = "Please, enter your email")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Please, enter your username")
    private String username;
    @NotEmpty(message = "Please, enter your password")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;
    private String confirmPassword;
}

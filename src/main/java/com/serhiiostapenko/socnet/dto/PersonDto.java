package com.serhiiostapenko.socnet.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
public class PersonDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String bio;
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;
}

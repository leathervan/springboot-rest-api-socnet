package com.serhiiostapenko.socnet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serhiiostapenko.socnet.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PersonDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String bio;
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    @JsonIgnore
    private String password;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.bio = person.getBio();
        this.password = person.getPassword();
    }
}

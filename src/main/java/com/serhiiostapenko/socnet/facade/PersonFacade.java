package com.serhiiostapenko.socnet.facade;

import com.serhiiostapenko.socnet.dto.PersonDto;
import com.serhiiostapenko.socnet.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonFacade {
    public PersonDto personToPersonDto(Person person){
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setUsername(person.getUsername());
        personDto.setPassword(person.getPassword());
        personDto.setBio(person.getBio());

        return personDto;
    }

}

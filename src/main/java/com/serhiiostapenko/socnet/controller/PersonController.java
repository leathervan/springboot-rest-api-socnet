package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.dto.PersonDto;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.facade.PersonFacade;
import com.serhiiostapenko.socnet.service.PersonService;
import com.serhiiostapenko.socnet.validator.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/socnet/person")
@CrossOrigin
public class PersonController {
    private final PersonService personService;
    private final PersonFacade personFacade;
    private final ResponseErrorValidator errorValidator;

    @Autowired
    public PersonController(PersonService personService, PersonFacade personFacade, ResponseErrorValidator errorValidator) {
        this.personService = personService;
        this.personFacade = personFacade;
        this.errorValidator = errorValidator;
    }

    @GetMapping("/")
    public ResponseEntity<PersonDto> getCurrentPerson(Principal principal){
        Person person = personService.getPersonFromPrincipal(principal);
        PersonDto personDto = personFacade.personToPersonDto(person);

        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable("id") long id){
        Person person = personService.getPersonById(id);
        PersonDto personDto = personFacade.personToPersonDto(person);

        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePersonById(Principal principal, @RequestBody @Valid PersonDto personDto, BindingResult bindingResult){
        ResponseEntity<Object> errors = errorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Person person = personService.updatePerson(personDto, principal);
        PersonDto updatedPersonDto = personFacade.personToPersonDto(person);

        return new ResponseEntity<>(updatedPersonDto, HttpStatus.OK);
    }
}

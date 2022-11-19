package com.serhiiostapenko.socnet.validator;

import com.serhiiostapenko.socnet.dto.request.SignupRequest;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserExistValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public UserExistValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target.getClass() == SignupRequest.class) {
            SignupRequest signupRequest = (SignupRequest) target;

            if (personService.getPersonByEmail(signupRequest.getEmail()) != null)
                errors.rejectValue("email", "", "This email is already taken");

            if (personService.getPersonByUsername(signupRequest.getUsername()) != null)
                errors.rejectValue("username", "", "This username is already taken");
        }
    }
}

package com.serhiiostapenko.socnet.validator;

import com.serhiiostapenko.socnet.annotations.PasswordMatches;
import com.serhiiostapenko.socnet.dto.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest personSignupRequest = (SignupRequest) o;
        return personSignupRequest.getPassword().equals(personSignupRequest.getConfirmPassword());
    }
}

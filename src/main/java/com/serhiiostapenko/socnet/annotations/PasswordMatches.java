package com.serhiiostapenko.socnet.annotations;

import com.serhiiostapenko.socnet.validator.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Passwords not matches";

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
}

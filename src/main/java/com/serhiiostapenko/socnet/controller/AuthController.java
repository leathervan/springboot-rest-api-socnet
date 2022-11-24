package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.dto.request.LoginRequest;
import com.serhiiostapenko.socnet.dto.request.SignupRequest;
import com.serhiiostapenko.socnet.dto.response.JWTTokenSuccessResponse;
import com.serhiiostapenko.socnet.dto.response.MessageResponse;
import com.serhiiostapenko.socnet.exception.UserExistException;
import com.serhiiostapenko.socnet.security.JWTTokenProvider;
import com.serhiiostapenko.socnet.security.SecurityConstants;
import com.serhiiostapenko.socnet.service.PersonService;
import com.serhiiostapenko.socnet.validator.ResponseErrorValidator;
import com.serhiiostapenko.socnet.validator.UserExistValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/socnet/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    private final ResponseErrorValidator validator;
    private final UserExistValidator existValidator;
    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider tokenProvider;

    @Autowired
    public AuthController(ResponseErrorValidator validator, UserExistValidator existValidator, PersonService personService, AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
        this.validator = validator;
        this.existValidator = existValidator;
        this.personService = personService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = validator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) {
        existValidator.validate(signupRequest,bindingResult);
        ResponseEntity<Object> errors = validator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        try {
            personService.createPerson(signupRequest);
            return new ResponseEntity<>(new MessageResponse("Success user registration"), HttpStatus.OK);
        } catch (UserExistException e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

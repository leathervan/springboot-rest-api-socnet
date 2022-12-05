package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.dto.PersonDto;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.enums.ERole;
import com.serhiiostapenko.socnet.exception.UserExistException;
import com.serhiiostapenko.socnet.dto.request.SignupRequest;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PersonService extends BasicService {
    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepo personRepo, PasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public Person createPerson(SignupRequest signupRequest){
        Person person = new Person();
        person.setEmail(signupRequest.getEmail());
        person.setUsername(signupRequest.getUsername());
        person.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        person.getRoles().add(ERole.ROLE_USER);

        try {
            log.info("Saving user " + person.getUsername());
            return personRepo.save(person);
        } catch (Exception e) {
            log.error("Error during registration: " + e.getMessage());
            throw new UserExistException("The user " + person.getUsername() + " already exist");
        }
    }
    @Transactional
    public Person updatePerson(PersonDto personDto, Principal principal){
        Person person = getPersonFromPrincipal(personRepo, principal);

        person.setUsername(personDto.getUsername());
        person.setBio(personDto.getBio());

        return personRepo.save(person);
    }

    public Person getPersonFromPrincipal(Principal principal){
        return getPersonFromPrincipal(personRepo, principal);
    }

    public Person getPersonById(long id) {
        return personRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public Person getPersonByUsername(String username) {
        return personRepo.findByUsername(username).orElse(null);
    }
    public Person getPersonByEmail(String email) {
        return personRepo.findByEmail(email).orElse(null);
    }
}

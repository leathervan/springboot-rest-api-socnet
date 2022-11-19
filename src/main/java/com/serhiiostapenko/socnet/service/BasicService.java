package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public abstract class BasicService {
    protected Person getPersonFromPrincipal(PersonRepo personRepo, Principal principal){
        String username = principal.getName();
        return personRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
    }
}

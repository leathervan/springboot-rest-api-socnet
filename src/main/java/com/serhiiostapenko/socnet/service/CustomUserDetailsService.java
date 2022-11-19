package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepo personRepo;

    @Autowired
    public CustomUserDetailsService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

        return personBuild(person);
    }

    public Person loadPersonById(Long id) {
         return personRepo.findById(id).orElse(null);
    }

    private static Person personBuild(Person person){
        List<GrantedAuthority> authorities = person.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new Person(person.getId(), person.getEmail(), person.getPassword(), person.getUsername(), authorities);
    }

}

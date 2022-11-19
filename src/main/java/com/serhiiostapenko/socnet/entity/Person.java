package com.serhiiostapenko.socnet.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serhiiostapenko.socnet.entity.enums.ERole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column(length = 3000)
    private String password;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(columnDefinition = "text")
    private String bio;

    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "person_id"))
    private Set<ERole> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "person", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    private void onCreate(){
        createdDate = LocalDateTime.now();
    }

    public Person(Long id, String email, String password, String username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

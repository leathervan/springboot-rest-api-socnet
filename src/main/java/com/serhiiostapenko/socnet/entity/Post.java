package com.serhiiostapenko.socnet.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption;
    private String location;

    @ManyToOne
    private Person person;

    @ManyToMany
    @JoinTable(name = "people_liked_post",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "person_id") })
    private Set<Person> peopleLikedPost = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    private void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public void addLike(Person person) {
        this.peopleLikedPost.add(person);
        person.getLikedPosts().add(this);
    }

    public void removeLike(Person person) {
        this.peopleLikedPost.remove(person);
        person.getLikedPosts().remove(this);
    }
}

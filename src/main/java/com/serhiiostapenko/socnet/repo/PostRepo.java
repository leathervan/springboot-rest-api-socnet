package com.serhiiostapenko.socnet.repo;

import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAllByPersonOrderByCreatedDateDesc(Person person);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndPerson(Long id, Person person);
}

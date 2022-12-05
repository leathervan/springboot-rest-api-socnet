package com.serhiiostapenko.socnet.repo;

import com.serhiiostapenko.socnet.entity.Comment;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByIdAndPerson(Long id, Person person);
}

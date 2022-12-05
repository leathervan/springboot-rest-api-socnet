package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.dto.CommentDto;
import com.serhiiostapenko.socnet.entity.Comment;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.Post;
import com.serhiiostapenko.socnet.exception.PostNotFoundException;
import com.serhiiostapenko.socnet.repo.CommentRepo;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import com.serhiiostapenko.socnet.repo.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CommentService extends BasicService {
    private final PostRepo postRepo;
    private final PersonRepo personRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(PostRepo postRepo, PersonRepo personRepo, CommentRepo commentRepo) {
        this.postRepo = postRepo;
        this.personRepo = personRepo;
        this.commentRepo = commentRepo;
    }
    @Transactional
    public Comment createComment(Long postId, CommentDto commentDto, Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);
        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + person.getUsername()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setPerson(person);
        comment.setMessage(commentDto.getMessage());

        log.info("Saving comment for post with id: " + post.getId());

        return commentRepo.save(comment);
    }

    public List<Comment> getAllCommentForPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<Comment> comments = commentRepo.findAllByPost(post);

        return comments;
    }
    @Transactional
    public void deleteComment(Long id){
        Optional<Comment> comment = commentRepo.findById(id);
        if(comment.isPresent()) commentRepo.delete(comment.get());
    }
}

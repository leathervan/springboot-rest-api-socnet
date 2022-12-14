package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.dto.PostDto;
import com.serhiiostapenko.socnet.entity.*;
import com.serhiiostapenko.socnet.exception.PostNotFoundException;
import com.serhiiostapenko.socnet.repo.ImageRepo;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import com.serhiiostapenko.socnet.repo.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PostService extends BasicService {
    private final PostRepo postRepo;
    private final PersonRepo personRepo;
    private final ImageRepo imageRepo;

    @Autowired
    public PostService(PostRepo postRepo, PersonRepo personRepo, ImageRepo imageRepo) {
        this.postRepo = postRepo;
        this.personRepo = personRepo;
        this.imageRepo = imageRepo;
    }

    @Transactional
    public Post createPost(PostDto postDto, Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);

        Post post = new Post();
        post.setPerson(person);
        post.setTitle(postDto.getTitle());
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());

        log.info("Saving post for user " + person.getUsername());

        return postRepo.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepo.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long id, Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);
        return postRepo.findPostByIdAndPerson(id, person).orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + person.getUsername()));
    }

    public List<Post> getAllPostsByPerson(Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);
        return postRepo.findAllByPersonOrderByCreatedDateDesc(person);
    }

    public List<Post> getAllPostsByPersonId(long id) {
        Person person = personRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id: " + id + " not found"));;
        return postRepo.findAllByPersonOrderByCreatedDateDesc(person);
    }

    @Transactional
    public Post likedPost(Long id, Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);
        Post post = postRepo.findById(id).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        if (post.getPeopleLikedPost().contains(person)) post.removeLike(person);
        else post.addLike(person);

        personRepo.save(person);
        return postRepo.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Optional<Image> image = imageRepo.findByPostId(id);
        if (image.isPresent()) imageRepo.delete(image.get());

        postRepo.deleteById(id);
    }
}

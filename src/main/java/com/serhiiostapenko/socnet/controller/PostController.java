package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.dto.PostDto;
import com.serhiiostapenko.socnet.entity.Post;
import com.serhiiostapenko.socnet.dto.response.MessageResponse;
import com.serhiiostapenko.socnet.service.PostService;
import com.serhiiostapenko.socnet.validator.ResponseErrorValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/socnet/post")
@CrossOrigin
public class PostController {
    private final PostService postService;
    private final ResponseErrorValidator errorValidator;

    public PostController(PostService postService, ResponseErrorValidator errorValidator) {
        this.postService = postService;
        this.errorValidator = errorValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> cratePost(Principal principal, @RequestBody @Valid PostDto postDto, BindingResult bindingResult) {
        ResponseEntity<Object> errors = errorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDto, principal);

        return new ResponseEntity<>(new PostDto(post), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : postService.getAllPosts()) {
            postDtos.add(new PostDto(post));
        }
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> getAllPostsByPersonId(@PathVariable("id") long id) {
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : postService.getAllPostsByPersonId(id)) {
            postDtos.add(new PostDto(post));
        }
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/person/posts")
    public ResponseEntity<List<PostDto>> getAllPostsByPerson(Principal principal) {
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : postService.getAllPostsByPerson(principal)) {
            postDtos.add(new PostDto(post));
        }
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<PostDto> likePost(Principal principal, @PathVariable("postId") long postId) {
        Post post = postService.likedPost(postId, principal);

        return new ResponseEntity<>(new PostDto(post), HttpStatus.OK);
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") long postId) {
        postService.deletePost(postId);

        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}

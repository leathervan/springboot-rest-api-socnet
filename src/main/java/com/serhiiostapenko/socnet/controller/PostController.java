package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.dto.PostDto;
import com.serhiiostapenko.socnet.entity.Post;
import com.serhiiostapenko.socnet.facade.PostFacade;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/socnet/post")
@CrossOrigin
public class PostController {
    private final PostService postService;
    private final PostFacade postFacade;
    private final ResponseErrorValidator errorValidator;

    public PostController(PostService postService, PostFacade postFacade, ResponseErrorValidator errorValidator) {
        this.postService = postService;
        this.postFacade = postFacade;
        this.errorValidator = errorValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> cratePost(Principal principal, @RequestBody @Valid PostDto postDto, BindingResult bindingResult){
        ResponseEntity<Object> errors = errorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDto, principal);
        PostDto createdPostDto = postFacade.postToPostDto(post);

        return new ResponseEntity<>(createdPostDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> postDtos = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/person/posts")
    public ResponseEntity<List<PostDto>> getAllPostsByPerson(Principal principal){
        List<PostDto> postDtos = postService.getAllPostsByPerson(principal)
                .stream()
                .map(postFacade::postToPostDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PostMapping("/like/{postId}/{username}")//todo personId instead of username
    public ResponseEntity<PostDto> likePost(@PathVariable("postId") long postId, @PathVariable("username") String username){
        Post post = postService.likedPost(postId,username);
        PostDto postDto = postFacade.postToPostDto(post);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PostMapping("/delete/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") long postId){
        postService.deletePost(postId);

        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}

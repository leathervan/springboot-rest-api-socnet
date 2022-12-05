package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.dto.CommentDto;
import com.serhiiostapenko.socnet.dto.PostDto;
import com.serhiiostapenko.socnet.entity.Comment;
import com.serhiiostapenko.socnet.dto.response.MessageResponse;
import com.serhiiostapenko.socnet.entity.Post;
import com.serhiiostapenko.socnet.service.CommentService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/socnet/comment")
@CrossOrigin
public class CommentController {
    private final CommentService commentService;
    private final ResponseErrorValidator errorValidator;

    public CommentController(CommentService commentService, ResponseErrorValidator errorValidator) {
        this.commentService = commentService;
        this.errorValidator = errorValidator;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(Principal principal,
                                                @PathVariable("postId") long postId,
                                                @RequestBody @Valid CommentDto commentDto,
                                                BindingResult bindingResult) {
        ResponseEntity<Object> errors = errorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.createComment(postId, commentDto, principal);

        return new ResponseEntity<>(new CommentDto(comment), HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable("postId") long postId) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : commentService.getAllCommentForPost(postId)) {
            commentDtos.add(new CommentDto(comment));
        }

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @PostMapping("/delete/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") long commentId) {
        commentService.deleteComment(commentId);

        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }
}

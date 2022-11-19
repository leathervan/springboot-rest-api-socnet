package com.serhiiostapenko.socnet.facade;

import com.serhiiostapenko.socnet.dto.CommentDto;
import com.serhiiostapenko.socnet.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public CommentDto commentToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setUsername(comment.getUsername());
        commentDto.setMessage(comment.getMessage());

        return commentDto;
    }
}

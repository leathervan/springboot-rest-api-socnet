package com.serhiiostapenko.socnet.dto;

import com.serhiiostapenko.socnet.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty
    private String message;
    private PersonDto personDto;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.personDto = new PersonDto(comment.getPerson());
    }
}

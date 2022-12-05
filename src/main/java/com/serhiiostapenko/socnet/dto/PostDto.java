package com.serhiiostapenko.socnet.dto;

import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String caption;
    private String location;
    private PersonDto personDto;
    private Set<PersonDto> peopleLikedPost;

    private Set<CommentDto> comments;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.caption = post.getCaption();
        this.location = post.getLocation();
        this.personDto = new PersonDto(post.getPerson());
        this.peopleLikedPost = post.getPeopleLikedPost()
                .stream()
                .map(PersonDto::new)
                .collect(Collectors.toSet());
        this.comments = post.getComments()
                .stream()
                .map(CommentDto::new)
                .collect(Collectors.toSet());
    }
}

package com.serhiiostapenko.socnet.facade;

import com.serhiiostapenko.socnet.dto.PostDto;
import com.serhiiostapenko.socnet.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    public PostDto postToPostDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setCaption(post.getCaption());
        postDto.setLocation(post.getLocation());
        postDto.setUsername(post.getPerson().getUsername());
        postDto.setLikes(post.getLikes());
        postDto.setUserLiked(post.getLikedUsers());

        return postDto;
    }
}

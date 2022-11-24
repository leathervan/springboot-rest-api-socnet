package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.entity.Image;
import com.serhiiostapenko.socnet.dto.response.MessageResponse;
import com.serhiiostapenko.socnet.exception.ImageNotFoundException;
import com.serhiiostapenko.socnet.exception.PostNotFoundException;
import com.serhiiostapenko.socnet.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/socnet/image")
@CrossOrigin
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToPerson(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        try {
            imageService.uploadImageToPerson(file, principal);
            return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
        } catch (IOException | PostNotFoundException | UsernameNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/upload/{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") long postId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        try {
            imageService.uploadImageToPost(file, principal, postId);
            return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
        } catch (IOException | PostNotFoundException | UsernameNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/avatar")
    public ResponseEntity<Object> getImageToPerson(Principal principal) {
        try {
            Image image = imageService.getImageToPerson(principal);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (ImageNotFoundException | UsernameNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getImageToPost(@PathVariable("postId") long postId) {
        try {
            Image image = imageService.getImageToPost(postId);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (ImageNotFoundException | UsernameNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}

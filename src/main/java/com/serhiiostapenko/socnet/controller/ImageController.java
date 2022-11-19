package com.serhiiostapenko.socnet.controller;

import com.serhiiostapenko.socnet.entity.Image;
import com.serhiiostapenko.socnet.dto.response.MessageResponse;
import com.serhiiostapenko.socnet.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        imageService.uploadImageToPerson(file, principal);

        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @PostMapping("/upload/{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") long postId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        imageService.uploadImageToPost(file, principal, postId);

        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @GetMapping("/avatar")
    public ResponseEntity<Image> getImageToPerson(Principal principal) {
        Image image = imageService.getImageToPerson(principal);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") long postId) {
        Image image = imageService.getImageToPost(postId);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}

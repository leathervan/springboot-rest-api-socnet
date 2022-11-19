package com.serhiiostapenko.socnet.service;

import com.serhiiostapenko.socnet.entity.Image;
import com.serhiiostapenko.socnet.entity.Person;
import com.serhiiostapenko.socnet.entity.Post;
import com.serhiiostapenko.socnet.exception.ImageNotFoundException;
import com.serhiiostapenko.socnet.exception.PostNotFoundException;
import com.serhiiostapenko.socnet.repo.ImageRepo;
import com.serhiiostapenko.socnet.repo.PersonRepo;
import com.serhiiostapenko.socnet.repo.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ImageService extends BasicService {
    private final ImageRepo imageRepo;
    private final PersonRepo personRepo;
    private final PostRepo postRepo;

    @Autowired
    public ImageService(ImageRepo imageRepo, PersonRepo personRepo, PostRepo postRepo) {
        this.imageRepo = imageRepo;
        this.personRepo = personRepo;
        this.postRepo = postRepo;
    }
    @Transactional
    public Image uploadImageToPerson(MultipartFile file, Principal principal) throws IOException {
        Person person = getPersonFromPrincipal(personRepo, principal);
        log.info("Uploading image profile to person:" + person.getUsername());

        Image personProfileImage = imageRepo.findByUserId(person.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(personProfileImage)) {
            imageRepo.delete(personProfileImage);
        }

        Image Image = new Image();
        Image.setUserId(person.getId());
        Image.setImageBytes(compressBytes(file.getBytes()));
        Image.setName(file.getOriginalFilename());
        return imageRepo.save(Image);
    }
    @Transactional
    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        Person person = getPersonFromPrincipal(personRepo, principal);
        Post post = postRepo.findPostByIdAndPerson(postId, person).orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + person.getUsername()));

        Optional<Image> imageOptional = imageRepo.findByPostId(post.getId());
        Image image;
        if(imageOptional.isEmpty()){
            image = new Image();
            image.setPostId(post.getId());
        } else image = imageOptional.get();

        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        log.info("Uploading image to post: " + post.getId());

        return imageRepo.save(image);
    }

    public Image getImageToPerson(Principal principal) {
        Person person = getPersonFromPrincipal(personRepo, principal);

        Image Image = imageRepo.findByUserId(person.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(Image)) {
            Image.setImageBytes(decompressBytes(Image.getImageBytes()));
        }

        return Image;
    }

    public Image getImageToPost(Long postId) {
        Image Image = imageRepo.findByPostId(postId).orElseThrow(() -> new ImageNotFoundException("Cannot find image to post: " + postId));

        if (!ObjectUtils.isEmpty(Image)) {
            Image.setImageBytes(decompressBytes(Image.getImageBytes()));
        }

        return Image;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

}

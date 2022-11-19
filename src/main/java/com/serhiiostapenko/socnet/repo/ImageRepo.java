package com.serhiiostapenko.socnet.repo;

import com.serhiiostapenko.socnet.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(Long id);

    Optional<Image> findByPostId(Long id);
}

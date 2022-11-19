package com.serhiiostapenko.socnet.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false, columnDefinition = "text")
    private String message;
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    private void onCreate() {
        createdDate = LocalDateTime.now();
    }
}

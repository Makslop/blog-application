package com.example.springbootblogapplication.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String title;

    @Column(columnDefinition = "TEXT")
    String body;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String imageFilePath;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;


}

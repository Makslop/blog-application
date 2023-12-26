package com.example.springbootblogapplication.services;

import com.example.springbootblogapplication.models.Comment;
import com.example.springbootblogapplication.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Optional<Comment> getById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}


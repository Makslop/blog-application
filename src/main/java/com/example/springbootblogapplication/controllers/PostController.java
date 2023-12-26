package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Comment;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.CommentService;
import com.example.springbootblogapplication.services.FileService;
import com.example.springbootblogapplication.services.PostService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.Session;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final AccountService accountService;
    private final FileService fileService;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private final EntityManager entityManager;
    @GetMapping("/posts/{id}")
    @Transactional
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setComments(new ArrayList<>(post.getComments()));

            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }
    }



    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post, @RequestParam("file") MultipartFile file) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            try {
                fileService.save(file);
                existingPost.setImageFilePath(file.getOriginalFilename());
            } catch (Exception e) {
                log.error("Error processing file: {}", file.getOriginalFilename());
            }

            postService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model) {

        Post post = new Post();
        model.addAttribute("post", post);
        return "post_new";
    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(@ModelAttribute Post post, @RequestParam("file") MultipartFile file, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Account account = accountService.findOneByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        try {
            fileService.save(file);
            post.setImageFilePath(file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Error processing file: {}", file.getOriginalFilename());
        }

        post.setAccount(account);
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model, Principal principal) {

        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (post.getAccount().getEmail().equals(authUsername)) {
                model.addAttribute("post", post);
                return "post_edit";
            } else {
                return "403";
            }
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }
    private final CommentService commentService;
    /*
    @PostMapping("/posts/{postId}/comments/new")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@PathVariable Long postId, @RequestParam String commentText, Principal principal) {
        Optional<Post> optionalPost = postService.getById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            Comment comment = new Comment();
            comment.setText(commentText);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setPost(post);

            String authUsername = "anonymousUser";
            if (principal != null) {
                authUsername = principal.getName();
                Account account = accountService.findOneByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));
                comment.setAccount(account);
            }

            post.getComments().add(comment);
            postService.save(post);

            return "redirect:/posts/" + postId;
        } else {
            return "404";
        }
    }


    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Optional<Comment> optionalComment = commentService.getById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            commentService.delete(comment);

            return "redirect:/posts/" + postId;
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getCommentForEdit(@PathVariable Long postId, @PathVariable Long commentId, Model model, Principal principal) {
        Optional<Comment> optionalComment = commentService.getById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            model.addAttribute("comment", comment);
            return "comment_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String newText) {
        Optional<Comment> optionalComment = commentService.getById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setText(newText);
            comment.setUpdatedAt(LocalDateTime.now());
            commentService.save(comment);

            return "redirect:/posts/" + postId;
        } else {
            return "404";
        }
    }
    */
    // PostController.java

    @PostMapping("/posts/{postId}/comments/new")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@PathVariable Long postId, @RequestParam String commentText, Principal principal) {
        Optional<Post> optionalPost = postService.getById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            Comment comment = new Comment();
            comment.setText(commentText);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setPost(post);

            String authUsername = "anonymousUser";
            if (principal != null) {
                authUsername = principal.getName();
                Account account = accountService.findOneByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));
                comment.setAccount(account);
            }

            post.getComments().add(comment);
            postService.save(post);

            return "redirect:/posts/" + postId;
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getCommentForEdit(@PathVariable Long postId, @PathVariable Long commentId, Model model, Principal principal) {
        Optional<Comment> optionalComment = commentService.getById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            // Check if the authenticated user is the author of the comment
            if (isCommentAuthor(comment, principal)) {
                model.addAttribute("comment", comment);
                return "comment_edit";
            } else {
                return "403"; // Access denied
            }
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Principal principal) {
        Optional<Comment> optionalComment = commentService.getById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            // Check if the authenticated user is the author of the comment
            if (isCommentAuthor(comment, principal)) {
                commentService.delete(comment);
                return "redirect:/posts/" + postId;
            } else {
                return "403"; // Access denied
            }
        } else {
            return "404";
        }
    }

    // Helper method to check if the authenticated user is the author of the comment
    private boolean isCommentAuthor(Comment comment, Principal principal) {
        if (principal != null) {
            String authUsername = principal.getName();
            return comment.getAccount().getEmail().equals(authUsername);
        }
        return false;
    }

}

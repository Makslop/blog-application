package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.FileService;
import com.example.springbootblogapplication.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final AccountService accountService;
    private final FileService fileService;
    private final PostService postService;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Long id, Model model) {

        Optional<Account> optionalAccount = this.accountService.getById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("profile", account);
            List<Post> posts = postService.getUserPosts(account.getId());
            model.addAttribute("posts", posts);
            return "profile";
        } else {
            return "404";
        }
    }




}

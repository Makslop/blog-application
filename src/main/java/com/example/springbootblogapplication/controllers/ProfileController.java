package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final AccountService accountService;
    private final FileService fileService;

    @GetMapping("/profile/{id}")
    public String getPost(@PathVariable Long id, Model model) {

        // find post by id
        Optional<Account> optionalAccount = this.accountService.getById(id);
        // if post exists put it in model
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("profile", account);
            return "profile";
        } else {
            return "404";
        }
    }




}

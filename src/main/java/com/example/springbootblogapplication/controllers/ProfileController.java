package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.FileService;
import com.example.springbootblogapplication.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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

    @GetMapping("/profile/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getProfileForEdit(@PathVariable Long id, Model model, Principal principal) {

        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        // find post by id
        Optional<Account> optionalProfile = accountService.getById(id);

        // if post exist and belongs to the authenticated user, put it in model
        if (optionalProfile.isPresent()) {
            Account profile = optionalProfile.get();

            // Check if the post belongs to the authenticated user
            if (profile.getEmail().equals(authUsername)) {
                model.addAttribute("profile", profile);
                return "profile_edit";
            } else {
                return "403";
            }
        } else {
            return "404";
        }
    }

    @PostMapping("/profile/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@PathVariable Long id, Account profile, @RequestParam("file") MultipartFile file) {

        Optional<Account> optionalProfile = accountService.getById(id);
        if (optionalProfile.isPresent()) {
            Account existingProfile = optionalProfile.get();

            existingProfile.setFirstName(profile.getFirstName());
            existingProfile.setLastName(profile.getLastName());
            existingProfile.setGender(profile.getGender());
            existingProfile.setAboutMe(profile.getAboutMe());
            try {
                fileService.save(file);
                existingProfile.setAvatarFilePath(file.getOriginalFilename());
            } catch (Exception e) {
                log.error("Error processing file: {}", file.getOriginalFilename());
            }

            accountService.save(existingProfile);
        }

        return "redirect:/profile/" + profile.getId();
    }




}

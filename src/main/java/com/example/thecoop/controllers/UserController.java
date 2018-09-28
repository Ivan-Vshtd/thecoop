package com.example.thecoop.controllers;

import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{

    @GetMapping
    public String userList(Model model){

        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @GetMapping("profile/{userId}")
    public String getProfile(Model model, @PathVariable Long userId){

        User user = userService.loadUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("email", user.getEmail());
        log.info(user.getUsername() + " -> user/profile");

        return "profile";
    }

    @PostMapping("profile/{userId}")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws IOException{

        saveAvatar(user, file);
        userService.updateProfile(user, password, email);
        log.info(user.getUsername() + " info has been successfully changed");

        return "redirect:/user/profile/" + userId;
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @PathVariable User user,
            @AuthenticationPrincipal User currentUser){

        userService.subscribe(currentUser, user);

        log.info(user.getUsername() + " -> /user/user-messages/" + user.getId());
        return "redirect:/user-messages/" + user.getId() + "/1";
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @PathVariable User user,
            @AuthenticationPrincipal User currentUser){

        userService.unsubscribe(currentUser, user);
        log.info(user.getUsername() + " -> /user/user-messages/" + user.getId());

        return "redirect:/user-messages/" + user.getId() + "/1";
    }

    @GetMapping("{type}/{user}/list")
    public String userList(Model model,
                           @PathVariable String type,
                           @PathVariable User user){

        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);
        if("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }
        log.info(user.getUsername() + " -> /user/subscription");

        return "subscription";
    }
}

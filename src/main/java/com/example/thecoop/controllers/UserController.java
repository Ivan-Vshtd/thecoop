package com.example.thecoop.controllers;

import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.example.thecoop.controllers.ControllerUtils.getInfo;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{

    @GetMapping
    public String userList(
            @AuthenticationPrincipal User user,
            Model model){

        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", user);
        log.info(user.getUsername() + " -> /user");

        return "userList";
    }

    @GetMapping("online")
    public String onlineUsers(
            @AuthenticationPrincipal User user,
            Model model){

        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAll()
                                                                   .stream()
                                                                   .filter(User::isOnline)
                                                                   .collect(Collectors.toSet()));
        model.addAttribute("isOnline", "online");
        log.info(user.getUsername() + " -> /user/online");

        return "userList";
    }

    @GetMapping("profile/{userId}")
    public String getProfile(Model model, @PathVariable Long userId){

        User user = userService.loadUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("info", user.getInfo());
        log.info(user.getUsername() + " -> user/profile");

        return "editProfile";
    }

    @PostMapping("profile/{userId}")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String location,
            @RequestParam String year,
            @RequestParam String month,
            @RequestParam String day,
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws IOException{

        saveAvatar(user, file);
        userService.updateProfile(user, password, email, getInfo(location, year, month, day));
        log.info(user.getUsername() + " info has been successfully changed");

        return "redirect:/user/profile/" + userId;
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @PathVariable User user,
            @AuthenticationPrincipal User currentUser){

        userService.subscribe(currentUser, user);
        log.info(user.getUsername() + " -> /user/user-profile/" + user.getId());

        return "redirect:/user-profile/" + user.getId() + "/1";
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @PathVariable User user,
            @AuthenticationPrincipal User currentUser){

        userService.unsubscribe(currentUser, user);
        log.info(user.getUsername() + " -> /user/user-profile/" + user.getId());

        return "redirect:/user-profile/" + user.getId() + "/1";
    }

    @GetMapping("{type}/{user}/list")
    public String userList(Model model,
                           @AuthenticationPrincipal User currentUser,
                           @PathVariable String type,
                           @PathVariable User user){

        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);
        if("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }
        model.addAttribute("user", currentUser);
        log.info(user.getUsername() + " -> /user/subscription");

        return "subscription";
    }
}

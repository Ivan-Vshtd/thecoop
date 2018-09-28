package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.Role;
import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author iveshtard
 * @since 9/18/2018
 */
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
@Controller
public class AdminController extends AbstractController {

    @GetMapping("/user/{user}")
    public String userEditForm(@PathVariable User user, Model model){

        authControl();

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        log.info(user.getUsername() + " -> /user");

        return "userEdit";
    }

    @PostMapping("/user")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            @RequestParam("file") MultipartFile file) throws IOException {

        authControl();

        saveAvatar(user, file);
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @RequestMapping("message-delete={message}")
    public String deleteMsgFromDb(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message) {

        authControl();

        deleteFile(message);

        List<Message> relatedMessages = messageRepo.findAllByAnswerMessage(message); // find all messages which were answer to this one
        relatedMessages.forEach(relMessage -> relMessage.setAnswerMessage(messageRepo.getOne(1L))); // set answer message with id 1 ('deleted')
        messageRepo.deleteById(message.getId());
        log.info(currentUser.getUsername() + " -> /user-messages/" + currentUser.getId()
                + " and successfully deleted the message from db");

        return "redirect:/main";
    }

    @RequestMapping("branch-delete={branch}")
    public String deleteBranch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Branch branch) {

        authControl();

        branchRepo.deleteById(branch.getId());
        log.info(currentUser.getUsername() + " -> /branches/" + currentUser.getId() + " and successfully deleted the branch");

        return "redirect:/branches";
    }
}
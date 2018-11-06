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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;

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

        saveAvatar(user, file);
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @RequestMapping("message-delete={message}")
    public String deleteMsgFromDb(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer) {

        deleteFile(message);

        List<Message> relatedMessages = messageService.findAllByAnswerMessage(message); // find all messages which were answer to this one
        relatedMessages.forEach(relMessage -> relMessage.setAnswerMessage(messageService.getOne(1L))); // set answer message with id 1 ('deleted')
        messageService.deleteById(message.getId());

        UriComponents components = getUriComponents(redirectAttributes, referer);
        log.info(currentUser.getUsername() + " -> /user-profile/" + currentUser.getId()
                + " and successfully deleted the message from db");

        return "redirect:" + components.getPath();
    }

    @RequestMapping("branch-delete={branch}")
    public String deleteBranch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Branch branch) {

        branchRepo.deleteById(branch.getId());
        log.info(currentUser.getUsername() + " -> /branches/" + currentUser.getId() + " and successfully deleted the branch");

        return "redirect:/branches";
    }
}

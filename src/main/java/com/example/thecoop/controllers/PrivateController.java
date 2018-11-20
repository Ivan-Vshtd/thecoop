package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.thecoop.utilities.AuthenticationController.onlineUsers;
import static com.example.thecoop.utilities.MessageHelper.markAsViewed;

/**
 * @author iveshtard
 * @since 9/13/2018
 */

@Slf4j
@Controller
@RequestMapping("/privates")
public class PrivateController extends AbstractController {

    @GetMapping("/{user}/branches")
    @PreAuthorize("authentication.name == #user")
    public String privateBranches(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String user, Model model) {

        List<Branch> userPrivateBranches = branchRepo
                .findBranchByDialogIsTrue()
                .stream()
                .filter(branch -> branch.getName()
                        .contains(currentUser.getUsername()))
                .collect(Collectors.toList()); // to get only user's private branches

        notifyUser(model, currentUser);

        model.addAttribute("branches", userPrivateBranches);
        model.addAttribute("user", currentUser);
        model.addAttribute("private", "private");
        log.info(currentUser.getUsername() + " -> (private) branches");

        return "branches";
    }

    @GetMapping("{user1}-{user2}/{number}")
    @PreAuthorize("authentication.name == #user1 || authentication.name == #user2")
    public String privateConversation(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String user1,
            @PathVariable String user2,
            @PathVariable int number,
            @RequestParam(required = false, defaultValue = "")
                    String filter, Model model) {

        User userOne = (User) userService.loadUserByUsername(user1);
        User userTwo = (User) userService.loadUserByUsername(user2);

        String branchName = String.join("-",
                Stream.of(userOne.getUsername(), userTwo.getUsername()).sorted().collect(Collectors.toList()));
        Branch branch = branchRepo.findByName(branchName);

        if (branch == null) {
            branch = new Branch();
        }
        branch.setName(branchName);
        branch.setDialog(true);

        List<Message> privateMessages = messageService.findAllByBranchIdOrderByDateDesc(branch.getId(), userOne);

        branchRepo.save(branch);
        privateMessages.forEach(message -> message.setDialog(true)); // set all messages of this brunch as private
        messageService.saveAll(privateMessages);

        List<Message> messages;
        PageRequest request = PageRequest.of(number - 1, MESSAGES_SIZE);
        Page<Message> page = messageService.findAllByBranchIdOrderByDateDesc(branch.getId(), request, userOne);
        messages = page.getContent();

        notifyUser(model, currentUser);
        markAsViewed(messages, currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userTwo);
        model.addAttribute("branch", branch);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("total", page.getTotalPages());
        model.addAttribute("current", number);
        model.addAttribute("onLineUsers", onlineUsers);
        log.info(currentUser.getUsername() + " -> branch and get private conversation");

        return "branch";
    }

    @PostMapping("{user1}-{user2}/{number}")
    @PreAuthorize("authentication.name == #user1 || authentication.name == #user2")
    public String addPrivateMessage(
            @AuthenticationPrincipal User user,
            @PathVariable String user1,
            @PathVariable String user2,
            @PathVariable int number,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {

        String branchName = String.join("-",
                Stream.of(user1, user2).sorted().collect(Collectors.toList()));

        Branch branch = branchRepo.findByName(branchName);

        if (branch == null) {
            branch = new Branch();
        }
        branch.setName(branchName);
        message.setAuthor(user);
        message.setBranch(branch);

        if (branch.isDialog()) {
            message.setDialog(true);
        }
        saveMessage(user, message, bindingResult, model, file);

        List<Message> messages;
        PageRequest request = PageRequest.of(number - 1, MESSAGES_SIZE);
        Page<Message> page = messageService.findAllByBranchIdOrderByDateDesc(branch.getId(), request, user);
        messages = page.getContent();

        model.addAttribute("messages", messages);
        model.addAttribute("branch", branch);
        log.info(user.getUsername() + " -> branch and successfully added private message");

        return "branch";
    }
}

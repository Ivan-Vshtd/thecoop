package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author iveshtard
 * @since 9/6/2018
 */

@Slf4j
@Controller
public class MessageController extends AbstractController {


    @RequestMapping("message-del={message}")
    public String delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message) {

        authControl();

        message.setText("message has been deleted by " + currentUser.getUsername());
        message.setDeleted(true);
        message.setUpdates(new Date());
        deleteFile(message);

        log.info(currentUser.getUsername() +
                " -> /user-messages/" + currentUser.getId() + " and successfully deleted the message");

        return "redirect:/main";
    }

    @GetMapping("/user-messages/{user}/{pageNumber}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @PathVariable int pageNumber,
            Model model,
            @RequestParam(required = false) Message message) {

        authControl();

        List<Message> messages;
        Page<Message> page;

        if (currentUser.equals(user) || currentUser.isAdmin()) {
            page = messageRepo.findMessageByAuthorOrderByDateDesc(user, request(pageNumber));
            messages = page.getContent();
        } else {
            page = messageRepo.findMessageByDialogIsFalseAndAuthorOrderByDateDesc(user, request(pageNumber));
            messages = page.getContent();
        }
        List<Branch> branches = branchRepo.findBranchByDialogIsFalse();

        model.addAttribute("user", currentUser);
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("current", pageNumber);
        model.addAttribute("total", page.getTotalPages());
        model.addAttribute("branches", branches);
        model.addAttribute("onLineUsers", onLineUsers());
        log.info(currentUser.getUsername() + " -> user-messages/" + user.getId() + "/" + pageNumber);

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}/{pageNumber}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable int pageNumber,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam(required = false) String branch,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {

        authControl();

        if (message != null && message.getAuthor().equals(currentUser)) {
            if (!Strings.isEmpty(text)) {
                message.setText(text);
            }
            if (!Strings.isEmpty(tag)) {
                message.setTag(tag);
            }
            if (!Strings.isEmpty(branch)) {
                message.setBranch(branchRepo.getOne(Long.valueOf(branch)));
            }

            if (!file.getOriginalFilename().isEmpty()) {
                saveMessageFile(message, file);
            }

            message.setUpdates(new Date());
            messageRepo.save(message);
            List<Branch> branches = branchRepo.findBranchByDialogIsFalse();
            log.info(currentUser.getUsername() + " -> /user-messages/" + user + " and successfully updated the message");

            model.addAttribute("branches", branches);
        }
        return "redirect:/user-messages/" + user + "/" + pageNumber;
    }

    @GetMapping("/answer/{userName}/{messageToAnswer}")
    @PreAuthorize("authentication.name == #userName")
    public String answer(
            @PathVariable Message messageToAnswer,
            @PathVariable String userName,
            Model model){

        authControl();

        if (messageToAnswer.isDialog() && !messageToAnswer
                                                    .getBranch()
                                                    .getName()
                                                    .contains(userName)){
                                        // to avoid ability get private message by url (will try to find another way)
            return "redirect:/main";
        }

        String answerTo = messageToAnswer.getAuthorName() + " said: " + messageToAnswer.getText();
        model.addAttribute("answerTo", answerTo);

        return "answer";
    }

    @PostMapping("/answer/{userName}/{messageToAnswer}")
    @PreAuthorize("authentication.name == #userName")
    public String answerToMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message messageToAnswer,
            @PathVariable String userName,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {

        authControl();

        message.setAnswerMessage(messageToAnswer);
        message.setAuthor(currentUser);
        message.setBranch(messageToAnswer.getBranch());
        message.setDialog(messageToAnswer.isDialog());
        saveMessage(currentUser, message, bindingResult, model, file);

        return "redirect:/main";
    }
}

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.example.thecoop.utilities.AuthenticationController.onlineUsers;

/**
 * @author iveshtard
 * @since 9/6/2018
 */

@Slf4j
@Controller
public class MessageController extends AbstractController {


    @RequestMapping("message-erase={message}")
    public String delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer) {

        message.setText("message has been deleted by " + currentUser.getUsername());
        message.setDeleted(true);
        message.setUpdates(new Date());
        deleteFile(message);

        log.info(currentUser.getUsername() +
                " -> /user-messages/" + currentUser.getId() + " and successfully deleted the message");

        UriComponents components = getUriComponents(redirectAttributes, referer);

        return "redirect:" + components.getPath();
    }

    @GetMapping("/user-messages/{user}/{pageNumber}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @PathVariable int pageNumber,
            Model model,
            @RequestParam(required = false) Message message) {

        List<Message> messages;
        Page<Message> page;

        if (currentUser.equals(user) || currentUser.isAdmin()) {
            page = messageService.findMessageByAuthorOrderByDateDesc(user, request(pageNumber), currentUser);
            messages = page.getContent();
        } else {
            page = messageService.findMessageByDialogIsFalseAndAuthorOrderByDateDesc(user, request(pageNumber), currentUser);
            messages = page.getContent();
        }
        List<Branch> branches = branchRepo.findBranchByDialogIsFalse();
        notifyUser(model, currentUser);

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
        model.addAttribute("onLineUsers", onlineUsers);
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
            messageService.save(message);
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
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestHeader String referer){

        if (messageToAnswer.isDialog() && !messageToAnswer
                                                    .getBranch()
                                                    .getName()
                                                    .contains(userName)){
                                        // to avoid ability get private message by url (will try to find another way)
            return "redirect:/main";
        }

        UriComponents components = getUriComponents(redirectAttributes, referer);

        String answerTo = messageToAnswer.getAuthorName() + " said: " + messageToAnswer.getText();
        model.addAttribute("answerTo", answerTo);
        model.addAttribute("path", components.getPath());

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
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path) throws IOException {

        message.setAnswerMessage(messageToAnswer);
        message.setAuthor(currentUser);
        message.setBranch(messageToAnswer.getBranch());
        message.setDialog(messageToAnswer.isDialog());
        saveMessage(currentUser, message, bindingResult, model, file);

        return "redirect:" + path;
    }

    @GetMapping("/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ){
        Set<User> likes = message.getLikes();
        if(likes.contains(currentUser)){
            likes.remove(currentUser);
        }
        else {
            likes.add(currentUser);
        }

        UriComponents components = getUriComponents(redirectAttributes, referer);

        return "redirect:" + components.getPath();
    }

}

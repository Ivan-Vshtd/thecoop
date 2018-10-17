package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.thecoop.utilities.AuthenticationController.onlineUsers;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Controller
public class MainController extends AbstractController {

    @Autowired
    SessionRegistry sessionRegistry;

    @GetMapping("/")
    public String login(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {
            log.info(user.getUsername() + " -> greeting");
            notifyUser(model, user);
        }
        model.addAttribute("user", user);
        return "greeting";
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal User user){
        List<SessionInformation> userSessions = sessionRegistry.getAllSessions(user, false);
        userSessions.forEach(SessionInformation::expireNow);
        return "logout";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "")
                               String filter, Model model) {

        List<Message> messages;

        Page<Message> page = messageService.findMessageByDialogIsFalseOrderByDateDesc(request(1), user);

        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findByTagAndDialogIsFalseOrderByDateDesc(filter, user);
        }
        else {
            messages = page.getContent();
        }
        List<Branch> branches = branchRepo.findBranchByDialogIsFalse();
        notifyUser(model, user);

        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("branches", branches);
        model.addAttribute("onLineUsers",onlineUsers);
        log.info(user.getUsername() + " -> main");

        return "main";
    }
}

package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/")
    public String login(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {
            log.info(user.getUsername() + " -> greeting");
        }
        model.addAttribute("user", user);

        return "greeting";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "")
                               String filter, Model model) {

        List<Message> messages;

        Page<Message> page = messageRepo.findMessageByDialogIsFalseOrderByDateDesc(request(1));

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTagAndDialogIsFalseOrderByDateDesc(filter);
        }
        else {
            messages = page.getContent();
        }
        List<Branch> branches = branchRepo.findBranchByDialogIsFalse();

        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("branches", branches);
        model.addAttribute("onLineUsers",onlineUsers);
        log.info(user.getUsername() + " -> main");

        return "main";
    }
}

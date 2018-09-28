package com.example.thecoop.controllers;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import com.example.thecoop.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.thecoop.controllers.ControllerUtils.getErrors;

/**
 * @author iveshtard
 * @since 9/5/2018
 */
@Slf4j
@Controller
@RequestMapping("/branches")
public class BranchController extends AbstractController {

    @Autowired
    BranchService branchService;

    @GetMapping
    public String branches(Model model) {

        authControl();

        List<Branch> branches = branchRepo.findBranchByDialogIsFalse();

        model.addAttribute("branches", branches);
        log.info(" -> branches");

        return "branches";
    }

    @PostMapping
    public String addBranch(
            @AuthenticationPrincipal User user,
            @Valid Branch branch,
            BindingResult bindingResult,
            Model model) {

        authControl();

        List<Branch> branches;

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("branch", branch);
            errors.keySet().forEach(key -> log.error("have got an error: " + key + errors.get(key)));
        } else {
            model.addAttribute("brunch", null);

            if(!branchService.addBranch(branch)){
                branches = branchRepo.findBranchByDialogIsFalse();
                model.addAttribute("branchError", "Such topic already exist!");
                model.addAttribute("branches", branches);
                return "branches";
            }
            log.info(user.getUsername() + " -> /branches  and successfully added the branch");
        }

        branches = branchRepo.findBranchByDialogIsFalse();
        model.addAttribute("branches", branches);

        return "branches";
    }

    @PostMapping("edit/{branch}")
    public String editBranch(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Branch branch,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {

        authControl();

        branchService.editBranch(branch, currentUser, name, description);
        log.info(currentUser.getUsername() + " -> /branches/ and successfully updated name of the branch");

        return "redirect:/branches";
    }

    @GetMapping("edit/{branch}")
    public String editBranch(
            @PathVariable Branch branch,
            Model model) {

        authControl();

        model.addAttribute("branch", branch);
        log.info(" -> branchEdit");

        return "branchEdit";
    }

    @GetMapping("{branchName}/{number}")
    public String branch(@AuthenticationPrincipal User user,
                         @PathVariable String branchName,
                         @PathVariable int number,
                         Model model) {

        authControl();

        Branch branch = branchRepo.findByNameAndDialogIsFalse(branchName);

        List<Message> messages;
        Page<Message> page = messageRepo.findAllByBranchIdOrderByDateDesc(branch.getId(), request(number));
        messages = page.getContent();

        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        model.addAttribute("total", page.getTotalPages());
        model.addAttribute("current", number);
        model.addAttribute("branch", branch);
        model.addAttribute("onLineUsers", onLineUsers());
        log.info(user.getUsername() + " -> branch");

        return "branch";
    }

    @PostMapping("{branchName}/{number}")
    public String addMessageToBranch(
            @AuthenticationPrincipal User user,
            @PathVariable String branchName,
            @PathVariable int number,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        authControl();
        Branch branch = branchService.addMessageToBranch(branchName, user, message);
        saveMessage(user, message, bindingResult, model, file);

        List<Message> messages;
        Page<Message> page = messageRepo.findAllByBranchIdOrderByDateDesc(branch.getId(), request(number));
        messages = page.getContent();

        model.addAttribute("messages", messages);
        model.addAttribute("branch", branch);
        model.addAttribute("onLineUsers", onLineUsers());
        log.info(user.getUsername() + " -> branch and successfully added message to branch");

        if (bindingResult.hasErrors()){
            return "branch";
        }
        return "redirect:/branches/" + branchName + "/" + number;
    }
}

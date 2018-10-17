package com.example.thecoop.controllers;

import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import com.example.thecoop.repos.BranchRepo;
import com.example.thecoop.service.MessageService;
import com.example.thecoop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.thecoop.controllers.ControllerUtils.getErrors;
import static com.example.thecoop.controllers.ControllerUtils.parent;

/**
 * @author iveshtard
 * @since 9/6/2018
 */

@Slf4j
public abstract class AbstractController {
    static int MESSAGES_SIZE = 5;

    @Value("${upload.path}")
    String uploadPath;

    @Value("${upload.avatar.path}")
    private String uploadAvatarPath;

    @Autowired
    MessageService messageService;

    @Autowired
    BranchRepo branchRepo;

    @Autowired
    UserService userService;


    void saveMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("message", message);
            errors.keySet().forEach(key -> log.error("have got an error: " + key + errors.get(key)));
        } else {
            saveMessageFile(message, file);

            model.addAttribute("message", null);
            messageService.save(message);
            log.info(user.getUsername() + " -> successfully added the message");
        }
    }

    void saveMessageFile(
            @Valid Message message,
            @RequestParam("file") MultipartFile file)
            throws IOException {

        message.setFilename(saveFile(file, path(uploadPath)));
    }

    void saveAvatar(
            @Valid User user,
            @RequestParam("avatar") MultipartFile file)
            throws IOException {
        user.setAvatarFilename(saveFile(file, path(uploadAvatarPath)));
        userService.userSave(user);
    }

    private String saveFile(MultipartFile file, String path) throws IOException {
        String resultFilename = null;
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(path);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(path + "\\" + resultFilename));
            log.info(resultFilename + " successfully saved");
        }
        return resultFilename;
    }

    void deleteFile(@PathVariable Message message) {
        if (message.getFilename() != null) {
            try {
                new File(path(uploadPath) + message.getFilename()).delete();
            } catch (Exception e) {
                log.error("Can not delete the " + message.getFilename());
                e.printStackTrace();
            }
        }
    }

    private String path(String destination) {
        return parent(parent(parent(this
                .getClass()
                .getClassLoader()
                .getResource("")
                .getPath()))) + '/' + destination + '/';
    }

    PageRequest request(int number) {
        return PageRequest.of(number - 1, MESSAGES_SIZE);
    }

    UriComponents getUriComponents(
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer) {

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components
                .getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return components;
    }

    void notifyUser(Model model, User currentUser) {

        List<Message> privateMessages = messageService
                .findAll(currentUser)
                .stream()
                .filter(message -> message.getId() != 1)
                .filter(message -> !message.isMeRead())
                .filter(Message::isDialog)
                .filter(message -> message.getBranch().getName().contains(currentUser.getUsername()))
                .filter(message -> !currentUser.equals(message.getAuthor()))
                .collect(Collectors.toList());

        if (privateMessages.size() > 0) {
            String notification = "You have %d new private message(s) from: " +
                    String.join(", ", privateMessages
                            .stream()
                            .map(Message::getAuthorName)
                            .collect(Collectors.toSet()));
            model.addAttribute("notification", String.format(notification, privateMessages.size()));
        }
    }
}

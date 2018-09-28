package com.example.thecoop.controllers;

import com.example.thecoop.domain.User;
import com.example.thecoop.domain.dto.CaptchaResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static com.example.thecoop.controllers.ControllerUtils.getErrors;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
@Slf4j
@Controller
public class RegistrationController extends AbstractController{
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(){
        log.info("-> registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2")String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model){

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()){
            model.addAttribute("captchaError", "Please complete a captcha!");
        }

        boolean isConfirmEmpty = Strings.isEmpty(passwordConfirm);
        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Password confirmation can't be empty");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if(isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()){
            Map<String, String> errors = getErrors(bindingResult);
            model.mergeAttributes(errors);

            log.warn("registration was unsuccessful");
            return "registration";
        }

        if(!userService.addUser(user)){
            model.addAttribute("usernameError", "Such user already exist!");
            return "registration";
        }
        log.info(user.getUsername() + " was successfully registered -> login");

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Your email has been successfully activated!");
            log.info("email has been successfully activated!");
        }else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
            log.error("activation code is not found!");
        }
        return "login";
    }
}

package com.example.thecoop.controllers;

import com.example.thecoop.utilities.AuthenticationController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author iveshtard
 * @since 8/3/2018
 */
@Slf4j
public class ControllerUtils extends AbstractController{

    static Map<String, String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(getFieldErrorMapCollector());
    }

    private static Collector<FieldError, ?, Map<String, String>> getFieldErrorMapCollector() {

        log.info("collect errors");
        return Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
    }

    public static void startAuthControl() {
        AuthenticationController auth = new AuthenticationController();

        if(!AuthenticationController.isStarted()) {
            new Thread(auth).start();
            AuthenticationController.setStarted(true);
        }
    }
}

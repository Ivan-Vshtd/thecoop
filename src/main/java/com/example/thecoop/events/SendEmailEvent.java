package com.example.thecoop.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author iveshtard
 * @since 8/27/2018
 */

@Getter
@AllArgsConstructor
public class SendEmailEvent {   //class added as example to use in UserService, EmailSender, MvcConfig to send async messages
    private String emailTo;
    private String subject;
    private String message;
}

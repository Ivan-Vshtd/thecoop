package com.example.thecoop.utilities;

import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;

import java.util.List;

/**
 * @author iveshtard
 * @since 10/15/2018
 */
public class MessageHelper {

    public static void meLiked(List<Message> messageList, User user){
        messageList.forEach(message -> message.setMeLiked(message.getLikes().contains(user)));
    }

    public static void meRead(List<Message> messageList, User user){
        messageList.forEach(message -> message.setMeRead(message.getNotifies().contains(user)));
    }

    public static void markAsViewed(List<Message> messages, User currentUser){
        messages.forEach(message -> message.setMeRead(true));
        messages.forEach(message -> message.getNotifies().add(currentUser));
    }
}

package com.example.thecoop.service;

import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import com.example.thecoop.repos.MessageRepo;
import com.example.thecoop.utilities.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author iveshtard
 * @since 10/15/2018
 */

@Service
public class MessageService {

    @Autowired
    MessageRepo messageRepo;

    public void save(Message message) {
        messageRepo.save(message);
    }

    public List<Message> findAllByAnswerMessage(Message message) {
        return messageRepo.findAllByAnswerMessage(message);
    }

    public Message getOne(long l) {
        return messageRepo.getOne(l);
    }

    public void deleteById(Long id) {
        messageRepo.deleteById(id);
    }

    public Page<Message> findAllByBranchIdOrderByDateDesc(Long id, PageRequest request, User currentUser) {
        Page<Message> messages = messageRepo.findAllByBranchIdOrderByDateDesc(id, request);
        MessageHelper.meLiked(messages.getContent(), currentUser);
        MessageHelper.meRead(messages.getContent(), currentUser);
        return messages;
    }

    public Page<Message> findMessageByDialogIsFalseOrderByDateDesc(PageRequest request, User currentUser) {
        Page<Message> messages = messageRepo.findMessageByDialogIsFalseOrderByDateDesc(request);
        MessageHelper.meLiked(messages.getContent(), currentUser);
        MessageHelper.meRead(messages.getContent(), currentUser);
        return messages;
    }

    public List<Message> findByTagAndDialogIsFalseOrderByDateDesc(String filter, User currentUser) {
        List<Message> messages = messageRepo.findByTagAndDialogIsFalseOrderByDateDesc(filter);
        MessageHelper.meLiked(messages, currentUser);
        MessageHelper.meRead(messages, currentUser);
        return messages;
    }

    public Page<Message> findMessageByAuthorOrderByDateDesc(User user, PageRequest request, User currentUser) {
        Page<Message> messages = messageRepo.findMessageByAuthorOrderByDateDesc(user, request);
        MessageHelper.meLiked(messages.getContent(), currentUser);
        MessageHelper.meRead(messages.getContent(), currentUser);
        return messages;
    }

    public Page<Message> findMessageByDialogIsFalseAndAuthorOrderByDateDesc(User user, PageRequest request, User currentUser) {
        Page<Message> messages = messageRepo.findMessageByDialogIsFalseAndAuthorOrderByDateDesc(user, request);
        MessageHelper.meLiked(messages.getContent(), currentUser);
        MessageHelper.meRead(messages.getContent(), currentUser);
        return messages;
    }

    public List<Message> findAllByBranchIdOrderByDateDesc(Long id, User currentUser) {
        List<Message> messages = messageRepo.findAllByBranchIdOrderByDateDesc(id);
        MessageHelper.meLiked(messages, currentUser);
        MessageHelper.meRead(messages, currentUser);
        return messages;
    }

    public void saveAll(List<Message> privateMessages) {
        messageRepo.saveAll(privateMessages);
    }

    public List<Message> findAll(User currentUser) {
        List<Message> messages = messageRepo.findAll();
        MessageHelper.meLiked(messages, currentUser);
        MessageHelper.meRead(messages, currentUser);
        return messages;
    }
}

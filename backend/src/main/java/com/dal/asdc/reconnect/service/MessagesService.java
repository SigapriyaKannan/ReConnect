package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.dto.Chat.Message;
import com.dal.asdc.reconnect.model.Messages;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.MessagesRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessagesService {
    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    UsersRepository usersRepository;

    public boolean saveMessage(String senderEmail, String receiverEmail, String messageContent) {
        Optional<Users> sender = usersRepository.findByUserEmail(senderEmail);
        Optional<Users> receiver = usersRepository.findByUserEmail(receiverEmail);
        Messages message = new Messages();
        message.setSender(sender.get());
        message.setReceiver(receiver.get());
        message.setMessageContent(messageContent);
        message.setTime(new Date());
        message.setRead(false);
        messagesRepository.save(message);
        log.info("Message from {} to {} saved successfully", senderEmail, receiverEmail);
        return true;
    }

    public List<Message> getChatHistory(String senderEmail, String receiverEmail) {
        List<Messages> messages = messagesRepository.findChatHistory(senderEmail, receiverEmail);

        List<Message> chatHistoryResponseBodyList = new ArrayList<>();

        for (Messages message : messages) {
            Message messagetItem = new Message();
            messagetItem.setMessage(message.getMessageContent());
            messagetItem.setSenderEmail(message.getSender().getUserEmail());
            messagetItem.setSenderId(message.getSender().getUserID());
            messagetItem.setReceiverEmail(message.getReceiver().getUserEmail());
            messagetItem.setReceiverId(message.getReceiver().getUserID());
            messagetItem.setTimestamp(message.getTime());

            chatHistoryResponseBodyList.add(messagetItem);
        }
        log.info("Retrieved chat history between {} and {}", senderEmail, receiverEmail);
        return chatHistoryResponseBodyList;
    }
}

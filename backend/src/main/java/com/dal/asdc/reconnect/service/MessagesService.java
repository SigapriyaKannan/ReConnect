package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.dto.Chat.ChatHistoryResponseBody;
import com.dal.asdc.reconnect.model.Messages;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.MessagesRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessagesService
{
    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    UsersRepository usersRepository;

    public boolean saveMessage(String senderEmail, String to, String context)
    {
        Users sender = usersRepository.findByUserEmail(senderEmail);
        Users receiver = usersRepository.findByUserEmail(to);
        Messages message = new Messages();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(context);
        message.setTime(LocalDateTime.now());
        message.setRead(false);
        messagesRepository.save(message);
        return true;
    }

    public List<ChatHistoryResponseBody> getChatHistory(String senderEmail, String receiverEmail)
    {
        List<Messages> messages = messagesRepository.findChatHistory(senderEmail, receiverEmail);

        List<ChatHistoryResponseBody> chatHistoryResponseBodyList = new ArrayList<>();

        for(Messages message : messages)
        {
            ChatHistoryResponseBody chatHistoryResponseBody = new ChatHistoryResponseBody();

            chatHistoryResponseBody.setSender(message.getSender().getUserEmail().equals(senderEmail) ? true : false);
            chatHistoryResponseBody.setMessage(message.getMessageContent());
            chatHistoryResponseBody.setTimestamp(message.getTime());
            chatHistoryResponseBodyList.add(chatHistoryResponseBody);
        }
        return chatHistoryResponseBodyList;
    }
}

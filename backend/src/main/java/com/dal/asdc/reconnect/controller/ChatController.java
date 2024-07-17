package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.Chat.ChatHistoryResponseBody;
import com.dal.asdc.reconnect.dto.Chat.ChatMessageRequest;
import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ChatController
{
    @Autowired
    MessagesService messagesService;

//    @MessageMapping("/chat.send")
//    @SendTo("/topic/public")
//    public ChatMessageRequest sendMessage(@RequestBody ChatMessageRequest chatMessage)
//    {
//        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();
//        messagesService.saveMessage(senderEmail,chatMessage.getTo(),chatMessage.getContext());
//        return chatMessage;
//    }

    @PostMapping("/chat.send")
    @SendTo("/topic/public")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessageRequest chatMessage)
    {
        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();
        boolean messageSent =  messagesService.saveMessage(senderEmail,chatMessage.getTo(),chatMessage.getContext());
        if (messageSent) {
            return ResponseEntity.ok("Message sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }


    @GetMapping("/getChatHistory")
    public ResponseEntity<?> getChatHistory(@RequestParam("email") String receiverEmail)
    {
        var senderEmail =   SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChatHistoryResponseBody> list = messagesService.getChatHistory(senderEmail,receiverEmail);
        if(list != null) {
            Response<List<ChatHistoryResponseBody>> response = new Response<>(HttpStatus.OK.value(), "Messages Fetched", list);
            return ResponseEntity.ok(response);
        } else {
            Response<?> response = new Response<>(HttpStatus.CONFLICT.value(), "Messages Not Found", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


    }

}

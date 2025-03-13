package com.dsrl2024.spring_chat.controller;

import com.dsrl2024.spring_chat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint for user messages
    @MessageMapping("/send")
    public void sendMessage(Message message) {
        // Forward message to the recipient
        messagingTemplate.convertAndSend("/topic/" + message.getReceiver(), message);
    }

    // Endpoint to notify when a message is read
    @MessageMapping("/read")
    public void notifyRead(Message message) {
        // When a message is read, we send only "Seen" to the topic (no sender info)
        Message readMessage = new Message();
        readMessage.setContent("Seen");
        readMessage.setReceiver(message.getReceiver());  // Same receiver as the original message
        messagingTemplate.convertAndSend("/topic/" + message.getReceiver(), readMessage);
    }


    // Notify when typing
    @MessageMapping("/typing")
    public void notifyTyping(Message message) {
        messagingTemplate.convertAndSend("/topic/" + message.getSender(), message);
    }

}
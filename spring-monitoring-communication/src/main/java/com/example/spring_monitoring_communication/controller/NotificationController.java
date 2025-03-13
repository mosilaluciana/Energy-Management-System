package com.example.spring_monitoring_communication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyClients(UUID userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }
}
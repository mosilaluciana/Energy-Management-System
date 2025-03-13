package com.dsrl2024.spring_chat.model;

import lombok.Data;

@Data
public class Message {

    private String sender;
    private String receiver;
    private String content;
}

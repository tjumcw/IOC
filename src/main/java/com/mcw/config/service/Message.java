package com.mcw.config.service;

import com.mcw.config.annotation.Autowired;
import com.mcw.config.annotation.Component;
import com.mcw.config.annotation.Value;

@Component(value = "message")
public class Message {

    @Autowired
    private Content content;

    @Value("1")
    private Integer id;

    @Value("2024-02-12")
    private String timeStamp;



    public Message(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public Message() {
    }

    public void printContent() {
        System.out.println("Message content: " + content);
    }

    @Override
    public String toString() {
        return "Message{" +
                "content=" + content +
                ", id=" + id +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

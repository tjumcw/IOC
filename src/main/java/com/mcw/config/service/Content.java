package com.mcw.config.service;

import com.mcw.config.annotation.Component;

@Component
public class Content {
    private String content;


    public Content() {
    }

    public Content(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Content{" +
                "content='" + content + '\'' +
                '}';
    }
}

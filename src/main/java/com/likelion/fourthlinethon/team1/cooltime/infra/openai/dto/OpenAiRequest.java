package com.likelion.fourthlinethon.team1.cooltime.infra.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {
    private String model = "gpt-4o-mini";
    private List<Message> messages;
    private ResponseFormat response_format = new ResponseFormat("json_object");

    public OpenAiRequest(List<Message> messages) {
        this.messages = messages;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseFormat {
        private String type;
    }
}
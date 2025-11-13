package com.likelion.fourthlinethon.team1.cooltime.infra.openai.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OpenAiResponse {
    private List<Choice> choices;

    @Getter
    @AllArgsConstructor
    public static class Choice {
        private Message message;
    }

    @Getter
    public static class Message {
        private String role;
        private String content;
    }
}
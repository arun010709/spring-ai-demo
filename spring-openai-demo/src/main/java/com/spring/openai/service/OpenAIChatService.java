package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {

    private final ChatClient chatClient;

    public OpenAIChatService(ChatClient.Builder chatClientBuilder){
        this.chatClient=chatClientBuilder.build();
    }

    public String chatWithOpenAiLlm(String prompt){
        return chatClient.prompt(prompt).call().content();
    }
}

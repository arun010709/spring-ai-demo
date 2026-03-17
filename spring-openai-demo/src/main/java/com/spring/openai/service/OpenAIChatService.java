package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {

    private final ChatClient openAiChatClient;

    private final ChatClient ollamaChatClient;

    public OpenAIChatService(ChatClient openAiChatClient,ChatClient ollamaChatClient){
        this.openAiChatClient=openAiChatClient;
        this.ollamaChatClient=ollamaChatClient;
    }

    public String chatWithOpenApi(String prompt){
        return openAiChatClient.prompt(prompt).call().content();
    }

    public String chatWithOllama(String prompt){
        return ollamaChatClient.prompt(prompt).call().content();
    }
}

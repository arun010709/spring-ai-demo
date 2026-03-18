package com.spring.openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {
    //For multi-model two clients are needed
    /*private final ChatClient openAiChatClient;
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
    }*/

    private final ChatClient chatClient;

    public OpenAIChatService(ChatClient.Builder chatClientBuilder){
        this.chatClient=chatClientBuilder.build();
    }

    public String chatWithOpenAi(String message){
        return chatClient.prompt(message).call().content();
    }


}

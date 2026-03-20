package com.spring.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    //multi model approach
   /* @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel).build();
    }*/

    @Bean
    public ChatClient messageRoleChatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.defaultSystem("""
                You are an defence admin.
                If user will ask any question which don't reveal the number of aircraft
                for each category. Just give some general details about each fleet.
                """).build();
    }
}

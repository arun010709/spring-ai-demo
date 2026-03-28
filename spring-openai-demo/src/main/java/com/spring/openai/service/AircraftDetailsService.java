package com.spring.openai.service;

import com.spring.openai.dto.Aircraft;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
public class AircraftDetailsService {

    private final ChatClient chatClient;
    private final ChatClient memoryBasedChatClient;
    @Value("classpath:prompts/aircraft_system_template.st")
    private Resource aircraftSystemTemplate;

    public AircraftDetailsService(ChatClient.Builder chatClientBuilder,ChatClient memoryBasedChatClient){
        this.chatClient=chatClientBuilder.build();
        this.memoryBasedChatClient=memoryBasedChatClient;
    }

    public Aircraft getAircraftDetails(String message){
        return chatClient.prompt().
                system(aircraftSystemTemplate).
                user(message).call()
                .entity(Aircraft.class);
    }

    public List<String> getAircraftDetailsList(String message) {
        return chatClient.prompt()
                .system(aircraftSystemTemplate)
                .user(message).call()
                .entity(new ListOutputConverter());
    }

    public Map<String, Object> getAircraftDetailsMap(String message) {
        return chatClient.prompt()
                .system(aircraftSystemTemplate)
                .user(message).call()
                .entity(new MapOutputConverter());
    }

    public List<Aircraft> getAircraftList(String message) {
        return chatClient.prompt()
                .system(aircraftSystemTemplate)
                .user(message).call()
                .entity(new ParameterizedTypeReference<>() {});
    }

    public String chatMemoryBasedAircraftResponse(String message,String username) {
        return memoryBasedChatClient.prompt(message)
                //Overriding the default conversation id for better context management
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,username))
                .call()
                .content();
    }
}

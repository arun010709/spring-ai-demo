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

@Service
public class AircraftDetailsService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/aircraft_system_template.st")
    private Resource aircraftSystemTemplate;

    public AircraftDetailsService(ChatClient.Builder chatClientBuilder){
        this.chatClient=chatClientBuilder.build();
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
                .entity(new ParameterizedTypeReference<>() {
                });
    }
}

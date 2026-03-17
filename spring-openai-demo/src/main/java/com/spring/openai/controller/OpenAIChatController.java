package com.spring.openai.controller;

import com.spring.openai.service.OpenAIChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class OpenAIChatController {

    private final OpenAIChatService openAIChatService;

    public OpenAIChatController(OpenAIChatService openAIChatService){
        this.openAIChatService=openAIChatService;
    }

    @GetMapping("/ollama")
    private String chatWithOllama(String prompt){
        return openAIChatService.chatWithOllama(prompt);
    }

    @GetMapping("/openApi")
    private String chatWithOpenApi(String prompt){
        return openAIChatService.chatWithOpenApi(prompt);
    }
}

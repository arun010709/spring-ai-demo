package com.spring.openai.controller;

import com.spring.openai.service.OpenAIChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openai/api/")
public class OpenAIChatController {

    private final OpenAIChatService openAIChatService;

    public OpenAIChatController(OpenAIChatService openAIChatService){
        this.openAIChatService=openAIChatService;
    }

    @GetMapping("/chat")
    private String chatWithLlm(String prompt){
        return openAIChatService.chatWithOpenAiLlm(prompt);
    }
}

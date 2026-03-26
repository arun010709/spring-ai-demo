package com.spring.openai.controller;

import com.spring.openai.service.MessageChatService;
import com.spring.openai.service.OpenAIChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequestMapping("/api/")
public class OpenAIChatController {

    private final OpenAIChatService openAIChatService;
    private final MessageChatService messageChatService;

    public OpenAIChatController(OpenAIChatService openAIChatService,MessageChatService messageChatService){
        this.openAIChatService=openAIChatService;
        this.messageChatService=messageChatService;
    }
    //Multi-model chat
    /*@GetMapping("/ollama")
    private String chatWithOllama(String prompt){
        return openAIChatService.chatWithOllama(prompt);
    }

    @GetMapping("/openApi")
    private String chatWithOpenApi(String prompt){
        return openAIChatService.chatWithOpenApi(prompt);
    }*/

    @GetMapping("openAi")
    private String chatWithOpenApi(String message){
        return openAIChatService.chatWithOpenAi(message);
    }

    @GetMapping("fleet")
    private String getFleetDetails(String message){
        return messageChatService.getFleetDetailsV1(message);
    }

    @GetMapping("recruitment")
    private String getRecruitmentDetails(String age,String serviceType,String qualification,String questions){
        return messageChatService.recruitmentProcess(age,serviceType,qualification,questions);

    }

    @GetMapping("chatOptions")
    private String chatOptions(String message){
        return messageChatService.chatOptions(message);
    }

    //Stream LLM response using flux.
    /*@GetMapping(value="chatOptions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> chatOptions(String message){
        return messageChatService.chatOptions(message);

    }*/
}

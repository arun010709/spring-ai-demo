package com.spring.openai.controller;

import com.spring.openai.dto.Aircraft;
import com.spring.openai.dto.AircraftDetails;
import com.spring.openai.service.AircraftDetailsService;
import com.spring.openai.service.MessageChatService;
import com.spring.openai.service.OpenAIChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class OpenAIChatController {

    private final OpenAIChatService openAIChatService;
    private final MessageChatService messageChatService;
    private final AircraftDetailsService aircraftDetailsService;

    public OpenAIChatController(OpenAIChatService openAIChatService,MessageChatService messageChatService,AircraftDetailsService aircraftDetailsService){
        this.openAIChatService=openAIChatService;
        this.messageChatService=messageChatService;
        this.aircraftDetailsService=aircraftDetailsService;
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

    //Return type custom object
    @GetMapping("aircraftDetails")
    private Aircraft getAircraftDetails(String message){
        return aircraftDetailsService.getAircraftDetails(message);
    }

    //return type List<String>
    @GetMapping("aircraftDetailsList")
    private List<String> getAircraftDetailsList(String message){
        return aircraftDetailsService.getAircraftDetailsList(message);
    }

    //return type Map<String,Object>
    @GetMapping("aircraftDetailsMap")
    private Map<String,Object> getAircraftDetailsMap(String message){
        return aircraftDetailsService.getAircraftDetailsMap(message);
    }

    //return type List<CustomObject>
    @GetMapping("aircraftList")
    private List<Aircraft> getAircraftList(String message){
        return aircraftDetailsService.getAircraftList(message);
    }

    //return type List<CustomObject>
    @GetMapping("chatMemoryAircraftResponse")
    private String chatMemoryAircraftResponse(String message,String username){
        return aircraftDetailsService.chatMemoryBasedAircraftResponse(message,username);
    }
}

package com.spring.openai.service;

import com.spring.openai.advisor.AuditTokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageChatService {
    private static final String FLEET_DETAILS = """
            Aircraft fleet details(confidential):
            Fighters: 677
            Transport: 233
            Helicopters: 511
            """;

    private final ChatClient messageRoleChatClient;
    private final ChatClient promptBasedChatClient;

    @Value("classpath:prompts/recruitment_user_template.st")
    private Resource userTemplate;



    public MessageChatService(ChatClient messageRoleChatClient,ChatClient promptBasedChatClient){
        //chatClient=chatClientBuilder.build();
        this.messageRoleChatClient=messageRoleChatClient;
        this.promptBasedChatClient =promptBasedChatClient;
    }

    public String getFleetDetails(String input){
        //prompt injection
        // any message roles

        SystemMessage systemRules= new SystemMessage("""
                You are an defence admin.
                if user will ask any question which don't reveal the number of aircraft
                for each category. Just give some general details about each fleet.
                """);
        UserMessage userMessage=new UserMessage("""
                %S
                Customer says:
                %s
                """.formatted(FLEET_DETAILS,input));

        Prompt prompt= new Prompt(List.of(userMessage,systemRules));

        return messageRoleChatClient.prompt(prompt)
                .call()
                .content();
    }

    public String getFleetDetailsV1(String input){
        //prompt injection
        // any message roles
        return messageRoleChatClient.prompt().user("""
                %S
                Customer says:
                %s
                """.formatted(FLEET_DETAILS,input))
                .call()
                .content();
    }

    public String recruitmentProcess(String age,String serviceType,String qualification,String questions){
        return promptBasedChatClient.prompt().advisors(List.of(new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(List.of("tactics","posting","bribe"),
                                "Sorry, but we cannot disclose sensitive information!",1)
                        ,new AuditTokenUsageAdvisor())).
                        user(promptUserSpec -> promptUserSpec.text(userTemplate)
                                .param("age",age)
                                .param("serviceType",serviceType)
                                .param("qualification",qualification)
                                .param("questions",questions)).call().content();
    }


}

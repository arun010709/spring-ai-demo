package com.spring.openai.config;

import com.spring.openai.rag.SimpleFilteringPostProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

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

    @Value("classpath:prompts/recruitment_system_template.st")
    private Resource systemTemplate;

    @Bean
    public ChatClient messageRoleChatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.defaultSystem("""
                You are an defence admin.
                If user will ask any question which don't reveal the number of aircraft
                for each category. Just give some general details about each fleet.
                """).build();
    }

    @Bean
    public ChatClient promptBasedChatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.defaultSystem(systemTemplate).build();
    }

    @Bean
    public ChatClient chatOptionsChatClient(ChatClient.Builder chatClientBuilder){
        //chatOptions control behaviour of LLM response
        return chatClientBuilder
                .defaultOptions(ChatOptions.builder()
                        .model("gpt-4o")
                        .temperature(0.1)
                        .maxTokens(1000)
                        .frequencyPenalty(0.7)
                        .presencePenalty(0.7)
                        //.topK(1)
                        .topP(1d)
                        .stopSequences(List.of("}"))
                        .build())
                .build();
    }

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder().maxMessages(5).
                chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    @Bean
    public ChatClient memoryBasedChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory){
        //chatMemory to store context of the chat
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor,chatMemoryAdvisor))
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 ChatMemory chatMemory,
                                 RetrievalAugmentationAdvisor retrievalAugmentationAdvisor){
        //chatMemory to store context of the chat
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();

        Advisor memoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();

        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor,retrievalAugmentationAdvisor))
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                                     ChatClient.Builder chatClientBuilder){
        return RetrievalAugmentationAdvisor.builder()
                //pre-retrieval logic
                .queryTransformers(TranslationQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder)
                        .targetLanguage("english")
                        .build())
                .documentRetriever(
                VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
                //post retrieval logic
                .documentPostProcessors(new SimpleFilteringPostProcessor())
                .build();
    }
}

package com.spring.openai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:prompts/systemDataPrompt.st")
    private Resource template;

    public RAGController(ChatClient chatClient,
                         VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/connect")
    public String connectToRagAI(@RequestParam String prompt,
                                 @RequestHeader String username) {

        /*// R - Retrieval query-Without RAG Advisor
        SearchRequest searchRequest =
                SearchRequest.builder()
                        .query(prompt)
                        .topK(5) // retrieve top 3 relevant documents from vector store
                        .similarityThreshold(0.5) // search for documents with similarity match above 0.5
                        .build();


        List<Document> similarDocuments = vectorStore
                .similaritySearch(searchRequest);

        // A-Augmentation - processed fetched documents
        List<String> similarResults = similarDocuments.stream()
                .map(Document::getText)
                .toList();

        // A+ G - Augmentation(prompt+similar results) + Generation
        String results = chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec
                                .text(template)
                                .param("documents", similarResults))
                .advisors(adviceSpec ->
                        adviceSpec.param(CONVERSATION_ID, username)
                )//Augmentation
                .user(prompt)
                .call()
                .content();*/


        return chatClient.
                prompt().advisors(adviceSpec ->
                        adviceSpec.param(CONVERSATION_ID, username))
                .user(prompt).call().content();

    }
}
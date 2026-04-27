package com.spring.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RAGPDFDataLoader {

    private final VectorStore vectorStore;

    @Value("classpath:IAF_Recruitment.pdf")
    private Resource pdfResource;

    private RAGPDFDataLoader(VectorStore vectorStore){
        this.vectorStore=vectorStore;
    }

    @PostConstruct
    public void loadPDFData(){
        TikaDocumentReader documentReader = new TikaDocumentReader(pdfResource);
        TextSplitter splitter = TokenTextSplitter.builder().
                                withChunkSize(50).withMaxNumChunks(100)
                                .build();
        List<Document> textChunks = splitter.split(documentReader.get());
        vectorStore.add(textChunks);
    }
}

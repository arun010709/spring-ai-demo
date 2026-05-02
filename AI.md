# RAG Architecture: Retrieval-Augmented Generation

### **[R]etrieval**
This phase involves searching your external data source (the Vector Store) to find relevant information based on the user's input.

```java
// Configuring the search parameters
SearchRequest searchRequest =
        SearchRequest.builder()
                .query(prompt)
                .topK(5) 
                .similarityThreshold(0.5)
                .build();

// Executing the retrieval from the Vector Store
List<Document> similarDocuments = vectorStore
        .similaritySearch(searchRequest);
```

### **[A]ugmentation**
In this step, the raw data retrieved from the database is processed and "injected" into the prompt template. This provides the AI with the specific context it needs to answer accurately.

```java
// Extracting text from the documents
List<String> similarResults = similarDocuments.stream()
        .map(Document::getText)
        .toList();

// Augmenting the system prompt with the retrieved 'documents'
.system(promptSystemSpec ->
        promptSystemSpec
                .text(template)
                .param("documents", similarResults))
```

### **[G]eneration**
Finally, the augmented prompt (User Query + Retrieved Context) is sent to the Large Language Model (LLM) to generate a grounded, natural language response.

```java
// The final call to the LLM to generate the content
.user(prompt)
.call()
.content();
```

---

### **RetrievalAugmentationAdvisor and Transformers(pre and post processing logic**
* **Retrieval:** Fetching relevant context from a knowledge base (Vector DB) using embeddings.
* **Augmentation:** Combining the user's query with the retrieved context into a single prompt.
* **Generation:** Producing the final output using the LLM, now "informed" by the provided data.
"""

with open("RAG_Concept_Recap.md", "w") as f:
    f.write(content)


```
### **Interview Recap: RetrievalAugmentationAdvisor**

#### **1. TranslationQueryTransformer**
Normalizes multi-lingual input into a standard language before retrieval[cite: 2].
```java
.queryTransformers(TranslationQueryTransformer.builder()
    .chatClientBuilder(chatClientBuilder)
    .targetLanguage("english")
    .build())
```
*   **Purpose**: Enables cross-lingual search (e.g., French query vs. English docs)[cite: 2].
*   **Benefit**: Increases retrieval accuracy by aligning the query language with the index language[cite: 2].

---

#### **2. RetrievalAugmentationAdvisor**
Middleware that automates the RAG cycle within the `ChatClient`[cite: 2].
```java
.advisors(new RetrievalAugmentationAdvisor(retriever))
```
*   **Mechanism**: Intercepts the query, fetches context, and auto-injects it into the system prompt[cite: 2].
*   **Key Filters**: `topK` (quantity) and `similarityThreshold` (quality)[cite: 2].

---

#### **3. VectorStoreDocumentRetriever**
The engine responsible for semantic search using embeddings[cite: 2].
*   **Search**: Uses vector similarity (cosine, etc.) instead of keyword matching[cite: 2].
*   **Groundedness**: Prevents hallucinations by forcing the LLM to answer using only retrieved chunks[cite: 2].

---

#### **4. MessageChatMemoryAdvisor**
*   **Role**: Automatically manages conversation history[cite: 2].
*   **Benefit**: Essential for multi-turn chats where the model needs to "remember" previous context[cite: 2].

# Spring AI DocumentPostProcessors

In Spring AI, **DocumentPostProcessors** handle the *Post-Retrieval* phase, refining documents before they reach the LLM.

## Java Usage
```java
.documentPostProcessors(new SimpleFilteringPostProcessor())
```

## Role
Acts as a final filter or formatter after the vector search.

## SimpleFilteringPostProcessor
Programmatically removes documents based on metadata or custom rules to reduce "noise".

## Key Benefit
- Optimizes the context window by ensuring only high-quality, relevant text is injected.

## Use Case
- Ideal for stripping sensitive data (PII) or deduplicating similar content before the augmentation step.

# Features Comparison

| Feature | Pre-Processor | Post-Processor |
|---------|-----------------|--------------|
| **Timing** | | |
| Before Vector Search |  |  |
| After Vector Search |  |  |
| **Input** |  |  |
| Raw User Query |  |  |
| Retrieved Document Chunks |  |  |
| **Goal** |  |  |
| Improve search accuracy |  |  |
| Improve context quality |  |  |
| **Example** |  |  |
| TranslationQueryTransformer |  |  |
| SimpleFilteringPostProcessor |  |  |

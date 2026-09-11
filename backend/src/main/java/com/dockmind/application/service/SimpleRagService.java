package com.dockmind.application.service;

import com.dockmind.application.dto.RagAnswer;
import com.dockmind.application.dto.RagSource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SimpleRagService {
    // we need vector store and chat client whose bean is already there
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    //dataset
    public int seed(){ //Creates a few sample documents, splits them, and stores them in pgvector.
        List<Document> documents = new ArrayList<>();

        documents.add(new Document("""
                DockMind is a developer knowledge assistant.
                It ingests code repositories and answers questions using RAG.
                The system stores embeddings in PostgreSQL with pgvector.
                """,
                Map.of(
                        "source", "docs/overview.md",
                        "type", "documentation"
                )
        ));

        documents.add(new Document("""
                DockMind ingestion pipeline clones a repository,
                walks through files, filters irrelevant files,
                chunks code, and then stores embeddings.
                """,
                Map.of(
                        "source", "docs/ingestion.md",
                        "type", "documentation"
                )
        ));

        documents.add(new Document("""
                DockMind chat flow retrieves relevant chunks first,
                then sends the user question and context to the LLM.
                Answers should include citations.
                """,
                Map.of(
                        "source", "docs/chat.md",
                        "type", "documentation"
                )
        ));

        //split -> chunk -> vector store
        TextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.split(documents);
        vectorStore.add(chunks);
        return chunks.size();
    }

//    Does similarity search, builds a prompt with context, calls the LLM, and returns:
//    the answer
//    the source chunks used
//    This is a manual RAG implementation.
    public RagAnswer ask(String question){
        //search request (top k)-> vectore store similarity search -> chat client -> result stream
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(4)
                .build();

        java.util.List<Document> results = vectorStore.similaritySearch(searchRequest);
        if (results == null || results.isEmpty()) {
            return new RagAnswer("No relevant context found. Please seed data first or ask a different question.", List.of());
        }

        String context = buildContext(results);
        // Continue with the rest of the ask method
        String systemPrompt = """
                You are DockMind, a precise engineering assistant.
                Always respond in English.
                Answer using only the provided context.
                If the answer is not in the context, say you do not know.
                Keep the answer clear and technical.
                """;
        String userPrompt = """
                Context:
                %s

                Question:
                %s
                """.formatted(context, question);
        String answer = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
        // stream
        List<RagSource> sources = results.stream()
                .map(doc -> new RagSource(doc.getText() != null ? doc.getText() : doc.getFormattedContent(), doc.getMetadata()))
                .toList();
        return new RagAnswer(answer != null ? answer : "", sources);
    }

    public String buildContext(List<Document> documents) {
        StringBuilder stringBuilder = new StringBuilder();

        int index = 1;

        for (Document document : documents) {
            stringBuilder.append("### Source ")
                    .append(index++)
                    .append("\n");
            stringBuilder.append("source: ")
                    .append(document.getMetadata().getOrDefault("source", "unknown"))
                    .append("\n");
            String content = document.getText() != null ? document.getText() : document.getFormattedContent();
            stringBuilder.append(content).append("\n\n");
        }
        return stringBuilder.toString();
    }
}

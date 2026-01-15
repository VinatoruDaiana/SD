package com.example.customersupport.services.rules;

import com.example.customersupport.entities.ChatMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;
import java.util.List;

@Service
public class GeminiAiService {

    @Value("${ai.gemini.apiKey:}")
    private String apiKey;

    @Value("${ai.gemini.model:gemini-1.5-flash}")
    private String model;

    @Value("${ai.gemini.baseUrl:https://generativelanguage.googleapis.com}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiAiService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * Returnează răspunsul Gemini pentru întrebarea userului (+ context din istoric).
     * Dacă nu ai API key setat, întoarce un fallback safe.
     */
    public String generateReply(String userId, String userMessage, List<ChatMessage> history) {
        if (apiKey == null || apiKey.isBlank()) {
            return "AI support is not configured (missing GEMINI key). Please contact an administrator.";
        }

        try {
            String url = baseUrl + "/v1beta/models/" + model + ":generateContent?key=" + apiKey;

            // Build prompt cu context minim (EMS) + istoric scurt
            StringBuilder prompt = new StringBuilder();
            prompt.append("You are a helpful customer support assistant for an Energy Management System (EMS). ")
                    .append("Prefer EMS-related answers, but if the user asks a simple general question ")
                    .append("(e.g., basic math or definitions), answer it normally. ")
                    .append("Be concise.\n\n");


            if (history != null && !history.isEmpty()) {
                prompt.append("Conversation so far:\n");
                // history are mesaje cu flag fromBot in entity (vezi ChatMessage)
                for (ChatMessage m : history) {
                    prompt.append(m.isFromBot() ? "Assistant: " : "User: ")
                            .append(m.getText())
                            .append("\n");
                }
                prompt.append("\n");
            }

            prompt.append("User question: ").append(userMessage).append("\n");
            prompt.append("Assistant answer:");

            // Gemini payload
            ObjectNode root = mapper.createObjectNode();
            ArrayNode contents = mapper.createArrayNode();

            ObjectNode userContent = mapper.createObjectNode();
            userContent.put("role", "user");
            ArrayNode parts = mapper.createArrayNode();
            ObjectNode partText = mapper.createObjectNode();
            partText.put("text", prompt.toString());
            parts.add(partText);
            userContent.set("parts", parts);

            contents.add(userContent);
            root.set("contents", contents);

            // Optional: limitează răspunsul ca să nu fie enorm
            ObjectNode generationConfig = mapper.createObjectNode();
            generationConfig.put("temperature", 0.4);
            generationConfig.put("maxOutputTokens", 250);
            root.set("generationConfig", generationConfig);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(root), headers);

            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                return "AI support is temporarily unavailable. Please try again later.";
            }

            JsonNode json = mapper.readTree(resp.getBody());
            JsonNode textNode = json
                    .path("candidates").path(0)
                    .path("content")
                    .path("parts").path(0)
                    .path("text");

            String answer = textNode.isMissingNode() ? null : textNode.asText(null);
            if (answer == null || answer.isBlank()) {
                return "AI support returned an empty response. Please rephrase your question.";
            }

            return answer.trim();

        } catch (Exception e) {
            return "AI support error: " + e.getMessage();
        }
    }
}

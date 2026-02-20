package com.open.ai.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.open.ai.constants.KeyConstants;

public class ClientCallsConfig {

  private final static RestTemplate restTemplate = new RestTemplate();


  private static Map<String, List<Map<String, String>>> conversationMemory = new HashMap<>();

  public static String callGroq(String message) {

    List<Map<String, String>> history = conversationMemory.getOrDefault("user", new ArrayList<>());

    history.add(Map.of("role", "user", "content", message));

    if (history.size() > 3) {
      history = new ArrayList<>(history.subList(history.size() - 3, history.size()));
    }

    conversationMemory.put("user", history);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(KeyConstants.groqApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = Map.of("model", "llama-3.1-8b-instant", "messages", history,
        "temperature", 0, "max_tokens", 200);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    Map<String, Object> response = restTemplate
        .postForObject("https://api.groq.com/openai/v1/chat/completions", request, Map.class);

    String reply = extractGroqText(response);

    history.add(Map.of("role", "assistant", "content", reply));
    conversationMemory.put("user", history);

    return reply;
  }


  public static String extractGroqText(Map<String, Object> response) {

    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

    return (String) message.get("content");
  }

  /* ================= GEMINI ================= */

  public static String callGemini(String message) {

    Map<String, Object> body =
        Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", message)))));

    Map<String, Object> response = restTemplate.postForObject(
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
            + KeyConstants.geminiApiKey,
        body, Map.class);

    return extractGeminiText(response);
  }

  public static String extractGeminiText(Map<String, Object> response) {

    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");

    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");

    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

    return (String) parts.get(0).get("text");
  }

}



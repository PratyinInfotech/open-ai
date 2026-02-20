package com.open.ai.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.open.ai.request.ChatRequest;
import com.open.ai.response.ChatResponse;
import com.open.ai.service.AiClientService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class AiChatController {

  private final AiClientService aiClientService;

  @PostMapping
  public ChatResponse chat(@RequestBody ChatRequest request) {
    String reply = aiClientService.ask(request.getMessage());
    return new ChatResponse(reply);
  }
}

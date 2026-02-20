package com.open.ai.service;

import org.springframework.stereotype.Service;

@Service
public interface AiClientService {

  public String ask(String message);
}

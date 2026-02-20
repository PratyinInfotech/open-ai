package com.open.ai.service.impl;

import org.springframework.stereotype.Service;
import com.open.ai.config.ClientCallsConfig;
import com.open.ai.service.AiClientService;

@Service
public class AiClientServiceImpl implements AiClientService {


  @Override
  public String ask(String message) {
    try {
      return ClientCallsConfig.callGroq(message); // PRIMARY
    } catch (Exception ex) {
      return ClientCallsConfig.callGemini(message); // FALLBACK
    }
  }



  /* ================= GROQ ================= */


}

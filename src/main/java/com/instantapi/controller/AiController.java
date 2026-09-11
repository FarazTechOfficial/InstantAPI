package com.instantapi.controller;

import com.instantapi.dto.AiExplainRequest;
import com.instantapi.dto.AiExplainResponse;
import com.instantapi.dto.AiRequest;
import com.instantapi.dto.GeneratorRequest;
import com.instantapi.services.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    // turn a prompt into a ready-to-use GeneratorRequest
    @PostMapping("/understand")
    public GeneratorRequest understand(@RequestBody AiRequest request) {
        return aiService.understand(request.getPrompt());
    }

    // answer questions about the generated API
    @PostMapping("/explain")
    public AiExplainResponse explain(@RequestBody AiExplainRequest request) {
        return aiService.explain(request.getPrompt(), request.getServiceName(), request.getFields());
    }
}
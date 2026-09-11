package com.instantapi.controller;

import com.instantapi.dto.AiExplainRequest;
import com.instantapi.dto.AiExplainResponse;
import com.instantapi.dto.AiRequest;
import com.instantapi.dto.GeneratorRequest;
import com.instantapi.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/understand")
    public GeneratorRequest understand(@RequestBody AiRequest request) {
        return aiService.understand(request.getPrompt());
    }

    @PostMapping("/explain")
    public AiExplainResponse explain(@RequestBody AiExplainRequest request) {
        return aiService.explain(request.getPrompt(), request.getServiceName(), request.getFields());
    }
}

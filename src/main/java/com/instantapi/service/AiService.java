package com.instantapi.service;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.AiExplainResponse;

public interface AiService {
    GeneratorRequest understand(String prompt);
    AiExplainResponse explain(String prompt, String serviceName, java.util.List<com.instantapi.dto.ParameterRequest> fields);
}

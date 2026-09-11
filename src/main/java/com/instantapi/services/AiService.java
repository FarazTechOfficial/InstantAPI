package com.instantapi.services;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.AiExplainResponse;
import com.instantapi.dto.ParameterRequest;

import java.util.List;

public interface AiService {
    GeneratorRequest understand(String prompt);
    AiExplainResponse explain(String prompt, String serviceName, List<ParameterRequest> fields);
}
package com.instantapi.dto;

import java.util.List;

public class AiExplainRequest {
    private String prompt;
    private String serviceName;
    private List<ParameterRequest> fields;

    public AiExplainRequest() {}

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public List<ParameterRequest> getFields() { return fields; }
    public void setFields(List<ParameterRequest> fields) { this.fields = fields; }
}
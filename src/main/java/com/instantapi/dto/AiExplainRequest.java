package com.instantapi.dto;

public class AiExplainRequest {
    private String prompt;
    private String serviceName;
    private java.util.List<ParameterRequest> fields;

    public AiExplainRequest() {}

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public java.util.List<ParameterRequest> getFields() { return fields; }
    public void setFields(java.util.List<ParameterRequest> fields) { this.fields = fields; }
}

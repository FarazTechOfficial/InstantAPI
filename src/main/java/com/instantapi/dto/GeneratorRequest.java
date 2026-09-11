package com.instantapi.dto;

import java.util.List;

public class GeneratorRequest {
    private String serviceName;
    private List<ParameterRequest> parameters;

    public GeneratorRequest() {}

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public List<ParameterRequest> getParameters() { return parameters; }
    public void setParameters(List<ParameterRequest> parameters) { this.parameters = parameters; }
}

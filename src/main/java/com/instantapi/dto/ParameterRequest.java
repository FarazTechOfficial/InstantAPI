package com.instantapi.dto;

public class ParameterRequest {
    private String name;
    private String dataType;

    public ParameterRequest() {}

    public ParameterRequest(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}

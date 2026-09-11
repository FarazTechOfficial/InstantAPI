package com.instantapi.dto;

public class AiExplainResponse {
    private String answer;

    public AiExplainResponse() {}

    public AiExplainResponse(String answer) { this.answer = answer; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}

package com.instantapi.service;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import com.instantapi.dto.AiExplainResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiServiceImpl implements AiService {
    private final ChatClient chatClient;

    public AiServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public GeneratorRequest understand(String prompt) {
        String message = """
                Convert the user's REST API description into JSON.\n
                Return exactly this structure (no markdown, no explanation, no extra text):\n
                {"serviceName":"<EntityName>","parameters":[{"name":"id","dataType":"String"},{"name":"<fieldName>","dataType":"<Type>"}]}\n
                \n
                Rules:\n
                - serviceName is the singular Java class name of the main entity the user describes (e.g. "Student", "Employee", "Order").\n
                - always include id as the first parameter with dataType "String".\n
                - parameter names use camelCase.\n
                - supported types: String, Integer, Long, Double, Float, Boolean, LocalDate.\n
                - if the user lists specific fields, return exactly those.\n
                - if the user only names an entity WITHOUT listing fields (e.g. "student management system"), infer 3 to 5 common, sensible fields for that entity (never return only id).\n
                - if the user mentions a type not supported, choose the nearest supported type (BigDecimal -> Double, date -> LocalDate, int -> Integer, decimal -> Double).\n
                - do not invent unrelated fields when the user already listed fields.\n
                \n
                User description: """ + prompt;

        return chatClient.prompt().user(message).call().entity(GeneratorRequest.class);
    }

    @Override
    public AiExplainResponse explain(String prompt, String serviceName, List<ParameterRequest> fields) {
        StringBuilder context = new StringBuilder();
        context.append("The user generated a Spring Boot CRUD API called ").append(serviceName).append(".\n");
        context.append("Fields: ");
        if (fields != null) {
            for (int i = 0; i < fields.size(); i++) {
                ParameterRequest f = fields.get(i);
                context.append(f.getName()).append(" (").append(f.getDataType()).append(")");
                if (i < fields.size() - 1) context.append(", ");
            }
        }
        context.append(".\n\n");
        context.append("The project includes: Entity, DTO, Transformer, Repository, Service, ServiceImpl, Controller, pom.xml, application.properties.\n\n");
        context.append("User question: ").append(prompt).append("\n");
        context.append("Answer concisely in plain text. Do not use markdown.");

        String answer = chatClient.prompt().user(context.toString()).call().content();
        return new AiExplainResponse(answer);
    }
}

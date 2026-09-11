package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DTOGenerator {
    private final TemplateService templates;

    public DTOGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(GeneratorRequest request, String className, String packageName) {
        Map<String, String> vars = templates.commonVars(packageName, className);
        Map<String, String> dynamic = new LinkedHashMap<>();
        dynamic.put("imports", importsLine(request));
        dynamic.put("fieldDeclarations", fieldDeclarations(request));
        return templates.render("DTO.java", vars, dynamic);
    }

    private String importsLine(GeneratorRequest request) {
        return TemplateService.isLocalDate(request.getParameters()) ? "import java.time.LocalDate;" : "";
    }

    private String fieldDeclarations(GeneratorRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ParameterRequest p : request.getParameters()) {
            if (p.getName().equalsIgnoreCase("id")) continue;
            sb.append("    private ").append(dtoType(p.getDataType())).append(" ").append(p.getName()).append(";\n");
        }
        return sb.toString();
    }

    private String dtoType(String dataType) {
        if (dataType == null) return "String";
        switch (dataType) {
            case "String":
            case "Integer":
            case "Long":
            case "Double":
            case "Float":
            case "Boolean":
            case "LocalDate":
                return dataType;
            default:
                return "String";
        }
    }
}
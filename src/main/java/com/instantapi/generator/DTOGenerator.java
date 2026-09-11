package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

@Component
public class DTOGenerator {
    public String generate(GeneratorRequest request, String className, String packageName) {
        String idField = NameUtil.fieldName(className) + "Id";
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".dto;\n\n");
        code.append("import lombok.AllArgsConstructor;\n");
        code.append("import lombok.Getter;\n");
        code.append("import lombok.NoArgsConstructor;\n");
        code.append("import lombok.Setter;\n");
        if (request.getParameters().stream().anyMatch(p -> "LocalDate".equals(p.getDataType()))) code.append("import java.time.LocalDate;\n");
        code.append("\n@Getter\n@Setter\n@NoArgsConstructor\n@AllArgsConstructor\n");
        code.append("public class ").append(className).append("DTO {\n");
        code.append("    private String ").append(idField).append(";\n");
        for (ParameterRequest p : request.getParameters()) {
            if (p.getName().equalsIgnoreCase("id")) continue;
            code.append("    private ").append(type(p.getDataType())).append(" ").append(p.getName()).append(";\n");
        }
        code.append("}\n");
        return code.toString();
    }

    private String type(String t) {
        return switch (t) {
            case "String", "Integer", "Long", "Double", "Float", "Boolean", "LocalDate" -> t;
            default -> "String";
        };
    }
}
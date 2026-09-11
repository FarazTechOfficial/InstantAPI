package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

@Component
public class EntityGenerator {
    public String generate(GeneratorRequest request, String className, String packageName) {
        String idField = NameUtil.fieldName(className) + "Id";
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".entity;\n\n");
        code.append("import jakarta.persistence.*;\n");
        code.append("import lombok.AllArgsConstructor;\n");
        code.append("import lombok.Getter;\n");
        code.append("import lombok.NoArgsConstructor;\n");
        code.append("import lombok.Setter;\n");
        if (request.getParameters().stream().anyMatch(p -> "LocalDate".equals(p.getDataType()))) code.append("import java.time.LocalDate;\n");
        code.append("\n@Entity\n@Getter\n@Setter\n@NoArgsConstructor\n@AllArgsConstructor\n");
        code.append("@Table(name = \"").append(NameUtil.fieldName(className)).append("s\")\n");
        code.append("public class ").append(className).append(" {\n");
        code.append("    @Id\n    private String ").append(idField).append(";\n");
        for (ParameterRequest p : request.getParameters()) {
            if (p.getName().equalsIgnoreCase("id")) continue;
            code.append("    @Column(name = \"").append(p.getName()).append("\")\n");
            code.append("    private ").append(box(p.getDataType())).append(" ").append(p.getName()).append(";\n");
        }
        code.append("}\n");
        return code.toString();
    }

    private String box(String type) {
        return switch (type) {
            case "Integer", "Long", "Double", "Float", "Boolean", "LocalDate" -> type;
            default -> "String";
        };
    }
}
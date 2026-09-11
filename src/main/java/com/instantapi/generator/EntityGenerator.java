package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EntityGenerator {
    private final TemplateService templates;

    public EntityGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(GeneratorRequest request, String className, String packageName) {
        Map<String, String> vars = templates.commonVars(packageName, className);
        Map<String, String> dynamic = new LinkedHashMap<>();
        dynamic.put("imports", importsLine(request));
        dynamic.put("fieldDeclarations", fieldDeclarations(request));
        return templates.render("Entity.java", vars, dynamic);
    }

    private String importsLine(GeneratorRequest request) {
        return TemplateService.isLocalDate(request.getParameters()) ? "import java.time.LocalDate;" : "";
    }

    private String fieldDeclarations(GeneratorRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ParameterRequest p : request.getParameters()) {
            if (p.getName().equalsIgnoreCase("id")) continue;
            sb.append("\n    @Column(name = \"").append(p.getName()).append("\")")
              .append("\n    private ").append(baseType(p.getDataType())).append(" ").append(p.getName()).append(";");
        }
        return sb.toString();
    }

    private String baseType(String dataType) {
        if (dataType == null) return "String";
        switch (dataType) {
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
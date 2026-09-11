package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransformerGenerator {
    private final TemplateService templates;

    public TransformerGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generateInterface(String packageName) {
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put("package", packageName);
        return templates.render("TransformerInterface.java", vars);
    }

    public String generate(GeneratorRequest request, String className, String packageName) {
        Map<String, String> vars = templates.commonVars(packageName, className);
        Map<String, String> dynamic = new LinkedHashMap<>();
        dynamic.put("imports", TemplateService.isLocalDate(request.getParameters()) ? "import java.time.LocalDate;" : "");
        dynamic.put("toEntityBody", toEntityBody(request));
        dynamic.put("toDtoBody", toDtoBody(request));
        dynamic.put("toUpdateBody", toUpdateBody(request));
        return templates.render("Transformer.java", vars, dynamic);
    }

    private List<ParameterRequest> params(GeneratorRequest request) {
        List<ParameterRequest> list = new java.util.ArrayList<>();
        for (ParameterRequest p : request.getParameters()) {
            if (!p.getName().equalsIgnoreCase("id")) list.add(p);
        }
        return list;
    }

    private String toEntityBody(GeneratorRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            sb.append("        entity.set").append(cap).append("(dto.get").append(cap).append("());\n");
        }
        return sb.toString();
    }

    private String toDtoBody(GeneratorRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            sb.append("        dto.set").append(cap).append("(entity.get").append(cap).append("());\n");
        }
        return sb.toString();
    }

    private String toUpdateBody(GeneratorRequest request) {
        StringBuilder sb = new StringBuilder();
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            sb.append("        if (dto.get").append(cap).append("() != null) entity.set").append(cap).append("(dto.get").append(cap).append("());\n");
        }
        return sb.toString();
    }
}
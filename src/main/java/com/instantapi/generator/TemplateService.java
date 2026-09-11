package com.instantapi.generator;

import com.instantapi.dto.ParameterRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TemplateService {

    public String template(String name) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + name);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Missing template: " + name, e);
        }
    }

    // common values shared by every generated file for one entity
    public Map<String, String> commonVars(String packageName, String className) {
        String fieldName = NameUtil.fieldName(className);
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put("package", packageName);
        vars.put("className", className);
        vars.put("fieldName", fieldName);
        vars.put("fieldNamePlural", fieldName + "s");
        vars.put("tableName", fieldName + "s");
        vars.put("artifactId", className.toLowerCase() + "-api");
        vars.put("idField", fieldName + "Id");
        vars.put("idFieldCap", NameUtil.className(fieldName + "Id"));
        vars.put("notFoundMessage", "\"" + className + " Not Found\"");
        return vars;
    }

    // render a template by classpath name with a vars map
    public String render(String templateName, Map<String, String> vars) {
        return replaceVars(template(templateName), vars);
    }

    // same as above but with extra dynamic values merged on top
    public String render(String templateName, Map<String, String> vars, Map<String, String> dynamic) {
        Map<String, String> all = new LinkedHashMap<>(vars);
        all.putAll(dynamic);
        return render(templateName, all);
    }

    // maps {{key}} placeholders to their values in raw content
    private String replaceVars(String content, Map<String, String> vars) {
        String result = content;
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    static boolean isLocalDate(java.util.List<ParameterRequest> parameters) {
        for (ParameterRequest p : parameters) {
            if ("LocalDate".equals(p.getDataType())) return true;
        }
        return false;
    }

    static String importsLine(boolean localDate) {
        return localDate ? "import java.time.LocalDate;" : "";
    }
}
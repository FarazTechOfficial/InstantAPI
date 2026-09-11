package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import org.springframework.stereotype.Component;

@Component
public class TransformerGenerator {
    public String generateInterface(String packageName) {
        return "package " + packageName + ".transfer;\n\n" +
                "import java.util.List;\n\n" +
                "public interface Transformer<E, D> {\n" +
                "    D toDto(E entity);\n" +
                "    E toEntity(D dto);\n" +
                "    E toUpdate(E entity, D dto);\n" +
                "    List<D> toDtoList(List<E> entities);\n" +
                "    List<E> toEntityList(List<D> dtos);\n" +
                "}\n";
    }

    public String generate(GeneratorRequest request, String className, String packageName) {
        String idField = NameUtil.fieldName(className) + "Id";
        String field = NameUtil.fieldName(className);
        StringBuilder code = new StringBuilder();
        code.append("package ").append(packageName).append(".transfer;\n\n");
        code.append("import ").append(packageName).append(".dto.").append(className).append("DTO;\n");
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        if (hasLocalDate(request)) code.append("import java.time.LocalDate;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import java.util.ArrayList;\n");
        code.append("import java.util.List;\n\n");
        code.append("@Component\n");
        code.append("public class ").append(className).append("Transformer implements Transformer<").append(className).append(", ").append(className).append("DTO> {\n\n");

        String idCap = NameUtil.className(idField);

        // toEntity
        code.append("    @Override\n    public ").append(className).append(" toEntity(").append(className).append("DTO dto) {\n");
        code.append("        ").append(className).append(" entity = new ").append(className).append("();\n");
        code.append("        entity.set").append(idCap).append("(dto.get").append(idCap).append("());\n");
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            code.append("        entity.set").append(cap).append("(dto.get").append(cap).append("());\n");
        }
        code.append("        return entity;\n    }\n\n");

        // toDto
        code.append("    @Override\n    public ").append(className).append("DTO toDto(").append(className).append(" entity) {\n");
        code.append("        ").append(className).append("DTO dto = new ").append(className).append("DTO();\n");
        code.append("        dto.set").append(idCap).append("(entity.get").append(idCap).append("());\n");
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            code.append("        dto.set").append(cap).append("(entity.get").append(cap).append("());\n");
        }
        code.append("        return dto;\n    }\n\n");

        // toUpdate - only copy the fields the client actually sent
        code.append("    @Override\n    public ").append(className).append(" toUpdate(").append(className).append(" entity, ").append(className).append("DTO dto) {\n");
        code.append("        if (dto.get").append(idCap).append("() != null) entity.set").append(idCap).append("(dto.get").append(idCap).append("());\n");
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            code.append("        if (dto.get").append(cap).append("() != null) entity.set").append(cap).append("(dto.get").append(cap).append("());\n");
        }
        code.append("        return entity;\n    }\n\n");

        // toDtoList
        code.append("    @Override\n    public List<").append(className).append("DTO> toDtoList(List<").append(className).append("> entities) {\n");
        code.append("        List<").append(className).append("DTO> list = new ArrayList<>();\n");
        code.append("        for (").append(className).append(" entity : entities) {\n");
        code.append("            list.add(toDto(entity));\n");
        code.append("        }\n");
        code.append("        return list;\n    }\n\n");

        // toEntityList
        code.append("    @Override\n    public List<").append(className).append("> toEntityList(List<").append(className).append("DTO> dtos) {\n");
        code.append("        List<").append(className).append("> list = new ArrayList<>();\n");
        code.append("        for (").append(className).append("DTO dto : dtos) {\n");
        code.append("            list.add(toEntity(dto));\n");
        code.append("        }\n");
        code.append("        return list;\n    }\n");

        code.append("}\n");
        return code.toString();
    }

    private boolean hasLocalDate(GeneratorRequest request) {
        for (ParameterRequest p : request.getParameters()) {
            if ("LocalDate".equals(p.getDataType())) return true;
        }
        return false;
    }

    private java.util.List<ParameterRequest> params(GeneratorRequest request) {
        java.util.List<ParameterRequest> list = new java.util.ArrayList<>();
        for (ParameterRequest p : request.getParameters()) {
            if (!p.getName().equalsIgnoreCase("id")) list.add(p);
        }
        return list;
    }
}
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
        if (request.getParameters().stream().anyMatch(p -> "LocalDate".equals(p.getDataType()))) code.append("import java.time.LocalDate;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.stream.Collectors;\n\n");
        code.append("@Component\n");
        code.append("public class ").append(className).append("Transformer implements Transformer<").append(className).append(", ").append(className).append("DTO> {\n\n");

        // toEntity
        String idCap = NameUtil.className(idField);
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

        // toUpdate
        code.append("    @Override\n    public ").append(className).append(" toUpdate(").append(className).append(" entity, ").append(className).append("DTO dto) {\n");
        code.append("        if (dto.get").append(idCap).append("() != null) entity.set").append(idCap).append("(dto.get").append(idCap).append("());\n");
        for (ParameterRequest p : params(request)) {
            String cap = NameUtil.className(p.getName());
            code.append("        if (dto.get").append(cap).append("() != null) entity.set").append(cap).append("(dto.get").append(cap).append("());\n");
        }
        code.append("        return entity;\n    }\n\n");

        // toDtoList
        code.append("    @Override\n    public List<").append(className).append("DTO> toDtoList(List<").append(className).append("> entities) {\n");
        code.append("        return entities.stream().map(this::toDto).collect(Collectors.toList());\n    }\n\n");

        // toEntityList
        code.append("    @Override\n    public List<").append(className).append("> toEntityList(List<").append(className).append("DTO> dtos) {\n");
        code.append("        return dtos.stream().map(this::toEntity).collect(Collectors.toList());\n    }\n");

        code.append("}\n");
        return code.toString();
    }

    private java.util.List<ParameterRequest> params(GeneratorRequest request) {
        return request.getParameters().stream().filter(p -> !p.getName().equalsIgnoreCase("id"))
                .toList();
    }
}
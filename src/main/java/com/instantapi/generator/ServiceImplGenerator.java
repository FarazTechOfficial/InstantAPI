package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ServiceImplGenerator {
    public String generate(String className, String packageName) {
        String field = NameUtil.fieldName(className);
        String dto = className + "DTO";
        String notFound = "\"" + className + " Not Found\"";
        return "package " + packageName + ".servicesImpl;\n\n" +
                "import " + packageName + ".dto." + dto + ";\n" +
                "import " + packageName + ".entity." + className + ";\n" +
                "import " + packageName + ".exception.ResourceNotFoundException;\n" +
                "import " + packageName + ".helper.Helper;\n" +
                "import " + packageName + ".payload.PageableResponse;\n" +
                "import " + packageName + ".repository." + className + "Repository;\n" +
                "import " + packageName + ".services." + className + "Service;\n" +
                "import " + packageName + ".transfer." + className + "Transformer;\n" +
                "import org.springframework.data.domain.Page;\n" +
                "import org.springframework.data.domain.PageRequest;\n" +
                "import org.springframework.data.domain.Sort;\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "import java.util.UUID;\n\n" +
                "@Service\n" +
                "public class " + className + "ServiceImpl implements " + className + "Service {\n\n" +
                "    private final " + className + "Repository " + field + "Repository;\n" +
                "    private final " + className + "Transformer " + field + "Transformer;\n\n" +
                "    public " + className + "ServiceImpl(" + className + "Repository " + field + "Repository, " + className + "Transformer " + field + "Transformer) {\n" +
                "        this." + field + "Repository = " + field + "Repository;\n" +
                "        this." + field + "Transformer = " + field + "Transformer;\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + dto + " save(" + dto + " dto) {\n" +
                "        // new record, give it a fresh id\n" +
                "        " + className + " " + field + " = " + field + "Transformer.toEntity(dto);\n" +
                "        " + field + ".set" + NameUtil.className(field + "Id") + "(UUID.randomUUID().toString());\n" +
                "        return " + field + "Transformer.toDto(" + field + "Repository.save(" + field + "));\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public PageableResponse<" + dto + "> getAll(int pageSize, int pageNo, String sortDir, String sortedBy) {\n" +
                "        Sort sort = sortDir.equalsIgnoreCase(\"desc\") ? Sort.by(sortedBy).descending() : Sort.by(sortedBy).ascending();\n" +
                "        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);\n" +
                "        Page<" + className + "> all = " + field + "Repository.findAll(pageRequest);\n" +
                "        return Helper.getPageableResponse(all, " + field + "Transformer);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + dto + " getById(String id) {\n" +
                "        " + className + " " + field + " = " + field + "Repository.findById(id).orElseThrow(\n" +
                "                () -> new ResourceNotFoundException(" + notFound + "));\n" +
                "        return " + field + "Transformer.toDto(" + field + ");\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + dto + " update(String id, " + dto + " dto) {\n" +
                "        " + className + " " + field + " = " + field + "Repository.findById(id).orElseThrow(\n" +
                "                () -> new ResourceNotFoundException(" + notFound + "));\n" +
                "        " + field + "Transformer.toUpdate(" + field + ", dto);\n" +
                "        return " + field + "Transformer.toDto(" + field + "Repository.save(" + field + "));\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public void delete(String id) {\n" +
                "        " + field + "Repository.findById(id).orElseThrow(\n" +
                "                () -> new ResourceNotFoundException(" + notFound + "));\n" +
                "        " + field + "Repository.deleteById(id);\n" +
                "    }\n" +
                "}\n";
    }
}
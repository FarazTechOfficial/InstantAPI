package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ServiceGenerator {
    public String generate(String className, String packageName) {
        String dto = className + "DTO";
        return "package " + packageName + ".services;\n\n" +
                "import " + packageName + ".dto." + dto + ";\n" +
                "import " + packageName + ".payload.PageableResponse;\n\n" +
                "public interface " + className + "Service {\n" +
                "    " + dto + " save(" + dto + " dto);\n" +
                "    PageableResponse<" + dto + "> getAll(int pageSize, int pageNo, String sortDir, String sortedBy);\n" +
                "    " + dto + " getById(String id);\n" +
                "    " + dto + " update(String id, " + dto + " dto);\n" +
                "    void delete(String id);\n" +
                "}\n";
    }
}
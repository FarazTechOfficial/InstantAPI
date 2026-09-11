package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class RepositoryGenerator {
    public String generate(String className, String packageName) {
        return "package " + packageName + ".repository;\n\n" +
                "import " + packageName + ".entity." + className + ";\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.stereotype.Repository;\n\n" +
                "@Repository\n" +
                "public interface " + className + "Repository extends JpaRepository<" + className + ", String> {\n}\n";
    }
}
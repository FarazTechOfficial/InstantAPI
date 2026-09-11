package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ProjectGenerator {
    private final EntityGenerator entityGenerator;
    private final DTOGenerator dtoGenerator;
    private final TransformerGenerator transformerGenerator;
    private final RepositoryGenerator repositoryGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ServiceImplGenerator serviceImplGenerator;
    private final ControllerGenerator controllerGenerator;

    public ProjectGenerator(EntityGenerator entityGenerator, DTOGenerator dtoGenerator,
                            TransformerGenerator transformerGenerator, RepositoryGenerator repositoryGenerator,
                            ServiceGenerator serviceGenerator, ServiceImplGenerator serviceImplGenerator,
                            ControllerGenerator controllerGenerator) {
        this.entityGenerator = entityGenerator;
        this.dtoGenerator = dtoGenerator;
        this.transformerGenerator = transformerGenerator;
        this.repositoryGenerator = repositoryGenerator;
        this.serviceGenerator = serviceGenerator;
        this.serviceImplGenerator = serviceImplGenerator;
        this.controllerGenerator = controllerGenerator;
    }

    public String createProject(GeneratorRequest request) {
        String className = NameUtil.className(request.getServiceName());
        String fieldName = NameUtil.fieldName(request.getServiceName());
        String packageName = "com.instantapi." + fieldName;
        Path project = Paths.get("generated", className + "API");
        Path javaPath = project.resolve("src/main/java/" + packageName.replace('.', '/'));
        Path resources = project.resolve("src/main/resources");
        try {
            deleteFolder(project);
            Files.createDirectories(javaPath.resolve("entity"));
            Files.createDirectories(javaPath.resolve("dto"));
            Files.createDirectories(javaPath.resolve("transfer"));
            Files.createDirectories(javaPath.resolve("repository"));
            Files.createDirectories(javaPath.resolve("services"));
            Files.createDirectories(javaPath.resolve("servicesImpl"));
            Files.createDirectories(javaPath.resolve("controller"));
            Files.createDirectories(javaPath.resolve("payload"));
            Files.createDirectories(javaPath.resolve("exception"));
            Files.createDirectories(javaPath.resolve("helper"));
            Files.createDirectories(resources);

            write(project.resolve("pom.xml"), pom(className));
            write(resources.resolve("application.properties"), properties());
            write(javaPath.resolve(className + "Application.java"), application(className, packageName));
            write(javaPath.resolve("entity/" + className + ".java"), entityGenerator.generate(request, className, packageName));
            write(javaPath.resolve("dto/" + className + "DTO.java"), dtoGenerator.generate(request, className, packageName));
            write(javaPath.resolve("transfer/Transformer.java"), transformerGenerator.generateInterface(packageName));
            write(javaPath.resolve("transfer/" + className + "Transformer.java"), transformerGenerator.generate(request, className, packageName));
            write(javaPath.resolve("repository/" + className + "Repository.java"), repositoryGenerator.generate(className, packageName));
            write(javaPath.resolve("services/" + className + "Service.java"), serviceGenerator.generate(className, packageName));
            write(javaPath.resolve("servicesImpl/" + className + "ServiceImpl.java"), serviceImplGenerator.generate(className, packageName));
            write(javaPath.resolve("controller/" + className + "Controller.java"), controllerGenerator.generate(className, packageName));
            write(javaPath.resolve("payload/ApiResponseMessage.java"), apiResponseMessage(packageName));
            write(javaPath.resolve("payload/PageableResponse.java"), pageableResponse(packageName));
            write(javaPath.resolve("exception/ResourceNotFoundException.java"), resourceNotFound(packageName));
            write(javaPath.resolve("exception/BadApiException.java"), badApi(packageName));
            write(javaPath.resolve("exception/GlobalExceptionHandler.java"), globalExceptionHandler(packageName));
            write(javaPath.resolve("helper/Helper.java"), helper(packageName));

            Path zip = Paths.get("generated", className + "API.zip");
            Files.createDirectories(zip.getParent());
            zipFolder(project, zip);
            return zip.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not generate project", e);
        }
    }

    private void write(Path path, String content) throws IOException { Files.writeString(path, content); }

    private String application(String c, String p) {
        return "package " + p + ";\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class " + c + "Application {\n    public static void main(String[] args) {\n        SpringApplication.run(" + c + "Application.class, args);\n    }\n}\n";
    }

    private String properties() {
        return "spring.datasource.url=jdbc:h2:mem:testdb\n" +
                "spring.datasource.driver-class-name=org.h2.Driver\n" +
                "spring.datasource.username=sa\n" +
                "spring.datasource.password=\n" +
                "spring.jpa.hibernate.ddl-auto=update\n" +
                "spring.h2.console.enabled=true\n" +
                "server.port=8081\n";
    }

    private String pom(String c) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>3.4.5</version>
                        <relativePath/>
                    </parent>
                    <groupId>com.generated</groupId>
                    <artifactId>""" + c.toLowerCase() + "-api</artifactId>\n" +
                "    <version>1.0.0</version>\n    <properties><java.version>17</java.version></properties>\n" +
                "    <dependencies>\n" +
                "        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>\n" +
                "        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>\n" +
                "        <dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><scope>provided</scope></dependency>\n" +
                "        <dependency><groupId>com.h2database</groupId><artifactId>h2</artifactId><scope>runtime</scope></dependency>\n" +
                "    </dependencies>\n" +
                "    <build><plugins><plugin><groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId></plugin></plugins></build>\n" +
                "</project>\n";
    }

    private String apiResponseMessage(String p) {
        return "package " + p + ".payload;\n\n" +
                "import lombok.*;\n" +
                "import org.springframework.http.HttpStatus;\n\n" +
                "@Getter\n@Setter\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder\n" +
                "public class ApiResponseMessage {\n" +
                "    private String message;\n" +
                "    private HttpStatus status;\n" +
                "    private boolean success;\n" +
                "    private Object data;\n" +
                "}\n";
    }

    private String pageableResponse(String p) {
        return "package " + p + ".payload;\n\n" +
                "import lombok.*;\n" +
                "import java.util.List;\n\n" +
                "@Getter\n@Setter\n@NoArgsConstructor\n@AllArgsConstructor\n@Builder\n" +
                "public class PageableResponse<D> {\n" +
                "    private List<D> data;\n" +
                "    private int pageSize;\n" +
                "    private int totalElements;\n" +
                "    private int pageNo;\n" +
                "    private int totalPage;\n" +
                "    private boolean lastPage;\n" +
                "}\n";
    }

    private String resourceNotFound(String p) {
        return "package " + p + ".exception;\n\n" +
                "public class ResourceNotFoundException extends RuntimeException {\n" +
                "    public ResourceNotFoundException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n";
    }

    private String badApi(String p) {
        return "package " + p + ".exception;\n\n" +
                "public class BadApiException extends RuntimeException {\n" +
                "    public BadApiException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n";
    }

    private String globalExceptionHandler(String p) {
        return "package " + p + ".exception;\n\n" +
                "import " + p + ".payload.ApiResponseMessage;\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.annotation.ExceptionHandler;\n" +
                "import org.springframework.web.bind.annotation.RestControllerAdvice;\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n\n" +
                "    @ExceptionHandler(ResourceNotFoundException.class)\n" +
                "    public ResponseEntity<ApiResponseMessage> resourceNotFoundHandler(ResourceNotFoundException ex) {\n" +
                "        ApiResponseMessage message = ApiResponseMessage.builder()\n" +
                "                .message(ex.getMessage())\n" +
                "                .status(HttpStatus.NOT_FOUND)\n" +
                "                .success(true)\n" +
                "                .data(null)\n" +
                "                .build();\n" +
                "        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);\n" +
                "    }\n\n" +
                "    @ExceptionHandler(BadApiException.class)\n" +
                "    public ResponseEntity<ApiResponseMessage> badApiExceptionHandler(BadApiException ex) {\n" +
                "        ApiResponseMessage message = ApiResponseMessage.builder()\n" +
                "                .message(ex.getMessage())\n" +
                "                .status(HttpStatus.BAD_REQUEST)\n" +
                "                .success(true)\n" +
                "                .data(null)\n" +
                "                .build();\n" +
                "        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);\n" +
                "    }\n" +
                "}\n";
    }

    private String helper(String p) {
        return "package " + p + ".helper;\n\n" +
                "import " + p + ".payload.PageableResponse;\n" +
                "import " + p + ".transfer.Transformer;\n" +
                "import org.springframework.data.domain.Page;\n\n" +
                "import java.util.List;\n" +
                "import java.util.stream.Collectors;\n\n" +
                "public class Helper {\n\n" +
                "    public static <E, D> PageableResponse<D> getPageableResponse(Page<E> page, Transformer<E, D> transformer) {\n" +
                "        List<D> collect = page.getContent().stream().map(transformer::toDto).collect(Collectors.toList());\n" +
                "        PageableResponse<D> pageableResponse = new PageableResponse<>();\n" +
                "        pageableResponse.setData(collect);\n" +
                "        pageableResponse.setPageSize(page.getSize());\n" +
                "        pageableResponse.setLastPage(page.isLast());\n" +
                "        pageableResponse.setTotalElements((int) page.getTotalElements());\n" +
                "        pageableResponse.setPageNo(page.getNumber());\n" +
                "        pageableResponse.setTotalPage(page.getTotalPages());\n" +
                "        return pageableResponse;\n" +
                "    }\n" +
                "}\n";
    }

    private void zipFolder(Path folder, Path zipFile) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(folder).filter(Files::isRegularFile).forEach(path -> {
                try {
                    String entry = folder.relativize(path).toString().replace('\\', '/');
                    zip.putNextEntry(new ZipEntry(folder.getFileName() + "/" + entry));
                    Files.copy(path, zip);
                    zip.closeEntry();
                } catch (IOException e) { throw new RuntimeException(e); }
            });
        }
    }

    private void deleteFolder(Path path) throws IOException {
        if (!Files.exists(path)) return;
        Files.walk(path).sorted((a, b) -> b.compareTo(a)).forEach(p -> {
            try { Files.delete(p); } catch (IOException e) { throw new RuntimeException(e); }
        });
    }
}
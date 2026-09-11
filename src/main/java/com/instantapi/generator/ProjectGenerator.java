package com.instantapi.generator;

import com.instantapi.dto.GeneratorRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
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
    private final TemplateService templates;

    public ProjectGenerator(EntityGenerator entityGenerator, DTOGenerator dtoGenerator,
                            TransformerGenerator transformerGenerator, RepositoryGenerator repositoryGenerator,
                            ServiceGenerator serviceGenerator, ServiceImplGenerator serviceImplGenerator,
                            ControllerGenerator controllerGenerator, TemplateService templates) {
        this.entityGenerator = entityGenerator;
        this.dtoGenerator = dtoGenerator;
        this.transformerGenerator = transformerGenerator;
        this.repositoryGenerator = repositoryGenerator;
        this.serviceGenerator = serviceGenerator;
        this.serviceImplGenerator = serviceImplGenerator;
        this.controllerGenerator = controllerGenerator;
        this.templates = templates;
    }

    public String createProject(GeneratorRequest request) {
        String className = NameUtil.className(request.getServiceName());
        String fieldName = NameUtil.fieldName(request.getServiceName());
        String packageName = "com.instantapi." + fieldName;
        Map<String, String> vars = templates.commonVars(packageName, className);
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

            write(project.resolve("pom.xml"),                     templates.render("pom.xml", vars));
            write(resources.resolve("application.properties"),   templates.render("application.properties", vars));
            write(javaPath.resolve(className + "Application.java"), templates.render("Application.java", vars));
            write(javaPath.resolve("entity/" + className + ".java"), entityGenerator.generate(request, className, packageName));
            write(javaPath.resolve("dto/" + className + "DTO.java"), dtoGenerator.generate(request, className, packageName));
            write(javaPath.resolve("transfer/Transformer.java"), transformerGenerator.generateInterface(packageName));
            write(javaPath.resolve("transfer/" + className + "Transformer.java"), transformerGenerator.generate(request, className, packageName));
            write(javaPath.resolve("repository/" + className + "Repository.java"), repositoryGenerator.generate(className, packageName));
            write(javaPath.resolve("services/" + className + "Service.java"), serviceGenerator.generate(className, packageName));
            write(javaPath.resolve("servicesImpl/" + className + "ServiceImpl.java"), serviceImplGenerator.generate(className, packageName));
            write(javaPath.resolve("controller/" + className + "Controller.java"), controllerGenerator.generate(className, packageName));
            write(javaPath.resolve("payload/ApiResponseMessage.java"),      templates.render("ApiResponseMessage.java", vars));
            write(javaPath.resolve("payload/PageableResponse.java"),       templates.render("PageableResponse.java", vars));
            write(javaPath.resolve("exception/ResourceNotFoundException.java"), templates.render("ResourceNotFoundException.java", vars));
            write(javaPath.resolve("exception/BadApiException.java"),      templates.render("BadApiException.java", vars));
            write(javaPath.resolve("exception/GlobalExceptionHandler.java"), templates.render("GlobalExceptionHandler.java", vars));
            write(javaPath.resolve("helper/Helper.java"),                  templates.render("Helper.java", vars));

            Path zip = Paths.get("generated", className + "API.zip");
            Files.createDirectories(zip.getParent());
            zipFolder(project, zip);
            return zip.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not generate project", e);
        }
    }

    private void write(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    private void zipFolder(Path folder, Path zipFile) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Files.walk(folder).filter(Files::isRegularFile).forEach(path -> {
                try {
                    String entry = folder.relativize(path).toString().replace('\\', '/');
                    zip.putNextEntry(new ZipEntry(folder.getFileName() + "/" + entry));
                    Files.copy(path, zip);
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void deleteFolder(Path path) throws IOException {
        if (!Files.exists(path)) return;
        Files.walk(path).sorted((a, b) -> b.compareTo(a)).forEach(p -> {
            try {
                Files.delete(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
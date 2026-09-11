package com.instantapi.generator;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RepositoryGenerator {
    private final TemplateService templates;

    public RepositoryGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(String className, String packageName) {
        return templates.render("Repository.java", templates.commonVars(packageName, className));
    }
}
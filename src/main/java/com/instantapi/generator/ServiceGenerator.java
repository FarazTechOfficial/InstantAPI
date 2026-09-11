package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ServiceGenerator {
    private final TemplateService templates;

    public ServiceGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(String className, String packageName) {
        return templates.render("Service.java", templates.commonVars(packageName, className));
    }
}
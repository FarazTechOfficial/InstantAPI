package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ServiceImplGenerator {
    private final TemplateService templates;

    public ServiceImplGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(String className, String packageName) {
        return templates.render("ServiceImpl.java", templates.commonVars(packageName, className));
    }
}
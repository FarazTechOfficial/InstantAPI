package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ControllerGenerator {
    private final TemplateService templates;

    public ControllerGenerator(TemplateService templates) {
        this.templates = templates;
    }

    public String generate(String className, String packageName) {
        return templates.render("Controller.java", templates.commonVars(packageName, className));
    }
}
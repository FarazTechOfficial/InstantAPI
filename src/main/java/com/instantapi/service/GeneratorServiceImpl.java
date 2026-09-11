package com.instantapi.service;

import com.instantapi.dto.GeneratorRequest;
import com.instantapi.dto.ParameterRequest;
import com.instantapi.generator.ProjectGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private final ProjectGenerator projectGenerator;

    public GeneratorServiceImpl(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    @Override
    public String generate(GeneratorRequest request) {
        // the generator needs a name to work with
        if (request.getServiceName() == null || request.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name is required");
        }
        if (request.getParameters() == null) request.setParameters(new ArrayList<>());

        // clean up the fields before generating
        List<ParameterRequest> parameters = new ArrayList<>();
        boolean hasId = false;
        for (ParameterRequest parameter : request.getParameters()) {
            if (parameter.getName() == null || parameter.getName().trim().isEmpty()) continue;
            if (parameter.getDataType() == null || parameter.getDataType().trim().isEmpty()) {
                parameter.setDataType("String");
            }
            if (parameter.getName().equalsIgnoreCase("id")) hasId = true;
            parameters.add(parameter);
        }
        // every API needs an id, so add one if it's missing
        if (!hasId) parameters.add(0, new ParameterRequest("id", "String"));
        request.setParameters(parameters);
        return projectGenerator.createProject(request);
    }
}
# INSTANTAPI

## An AI-Powered REST API Project Generator

### Project Report

---

# Abstract

InstantAPI is a web-based application that helps developers create REST API projects with less manual coding. The system allows a user to describe an API in simple English or enter the required fields manually. In AI mode, the system sends the user's description to an AI model and converts it into a structured API request. The user can then review and edit the generated fields before creating the project.

The backend of InstantAPI is developed using Java and Spring Boot. Spring AI is used to connect the application with an AI service through an OpenAI-compatible interface. The project uses Groq as the configured AI service. The generated API follows a fixed layered structure that includes Entity, DTO, Transformer, Repository, Service, Service Implementation, Controller, exception handling, and pagination support.

The system generates Java source files from templates and packages the generated project into a ZIP file. The generated project can then be downloaded and run as a separate Spring Boot application. InstantAPI also provides an AI explanation feature that allows the user to ask questions about the generated API.

The main purpose of InstantAPI is to reduce repetitive development work and provide a simple way to create a basic CRUD REST API project.

---

# Chapter 1: Introduction

## 1.1 Background

Developing a REST API usually requires creating several files even when the basic structure of the application is simple. A developer may need to create an entity, data transfer object, repository, service, service implementation, controller, exception classes, and other supporting files.

This work is useful, but much of it is repetitive. The developer must also maintain a similar structure across different projects. This can take time, especially when creating small APIs for testing, learning, or early development.

InstantAPI was developed to reduce this repeated work. Instead of manually creating all the basic files, the user can provide information about the API and let the system generate the project structure.

The system provides two main ways to define an API. The first is AI mode, where the user describes the API using normal language. The second is manual mode, where the user directly enters the service name and fields.

## 1.2 Problem Statement

Creating a basic CRUD API manually requires several development steps. The developer needs to create multiple Java classes and maintain relationships between them. Small changes to the entity can also require changes in several files.

For a simple API, this repeated work can be unnecessary. There is a need for a tool that can take basic API requirements and create a ready-to-run project structure automatically.

The problem addressed by InstantAPI is therefore:

**How can the creation of a basic Spring Boot CRUD REST API be made faster and simpler by using AI and automatic source-code generation?**

## 1.3 Aim of the Project

The main aim of InstantAPI is to develop a web application that can automatically generate a Spring Boot CRUD REST API project from user-defined API requirements.

The system also aims to make the process easier for users who may not want to manually create every basic project file.

## 1.4 Objectives

The main objectives of InstantAPI are:

1. To provide a simple web interface for creating APIs.
2. To allow users to describe an API using natural language.
3. To use AI to identify the main entity and its fields.
4. To allow users to review and edit AI-generated fields.
5. To provide a manual API creation option.
6. To generate Java Spring Boot source files automatically.
7. To create a standard layered project structure.
8. To generate basic CRUD operations.
9. To package the generated project as a ZIP file.
10. To provide an AI-based explanation feature for the generated API.

## 1.5 Scope

The current scope of InstantAPI is focused on generating basic Spring Boot CRUD APIs.

The system supports the following data types:

* String
* Integer
* Long
* Double
* Float
* Boolean
* LocalDate

The generated project contains common layers such as entity, DTO, transformer, repository, service, service implementation, controller, exception handling, and helper classes.

The application also supports pagination in the generated API.

The current implementation does not represent a complete general-purpose software generator. Its generation rules are focused on the supported project structure and supported data types.

## 1.6 Significance of the Project

InstantAPI can be useful for developers who need a basic CRUD API without creating every standard file manually. It can also be useful for students who are learning Spring Boot because the generated project shows how different application layers are connected.

The AI mode also provides a more natural way of describing an API. A user can write a description such as creating an API for products with name, price, and stock. The AI service then converts this description into structured information used by the generator.

---

# Chapter 2: Existing System and Proposed System

## 2.1 Existing Approach

In a traditional approach, a developer creates a Spring Boot project and manually writes the required classes. For a CRUD API, this normally includes an entity class, DTO, repository, service, service implementation, controller, and other supporting classes.

The developer must also write similar CRUD methods for different entities. This process is manageable for one project, but it becomes repetitive when several similar APIs are required.

## 2.2 Proposed System

InstantAPI provides an automated alternative.

The user can either describe an API in natural language or enter the API information manually. If AI mode is used, the description is sent to the configured AI service. The returned information contains the service name and parameters.

The user can review the result before generation. After confirmation, InstantAPI creates the required Java files using predefined templates and generators. The generated project is then compressed into a ZIP file and returned to the user.

This approach separates the AI interpretation step from the actual source-code generation. The AI is mainly used to understand the user's description, while deterministic Java generator classes create the project files.

This separation is important because the final project structure does not depend on the AI generating complete Java source code directly.

---

# Chapter 3: System Requirements

## 3.1 Functional Requirements

The system provides the following main functions.

### AI API Understanding

The user can enter an API description in natural language. The AI service converts the description into a structured GeneratorRequest.

### Manual API Creation

The user can manually enter the service name and fields without using the AI feature.

### Field Editing

AI-generated fields can be edited before project generation. Users can add or remove fields and change their data types.

### Project Generation

The system creates the required Spring Boot project files based on the selected entity and fields.

### ZIP Download

The generated project is compressed into a ZIP file and downloaded by the user.

### AI Explanation

The user can ask questions about the generated API. The AI service receives information about the API and provides a plain-text explanation.

## 3.2 Non-Functional Requirements

The application should provide a simple user interface and should respond correctly to valid requests.

The generated projects should follow a consistent structure. The system should also handle invalid or missing service information before starting the generation process.

The application should be easy to run using Java and Maven.

---

# Chapter 4: Technologies Used

## 4.1 Java

Java is the main programming language used in the backend. The project is configured for Java 17.

## 4.2 Spring Boot

Spring Boot is used to build the backend web application. It provides the REST controller system and application configuration required to run InstantAPI.

The project uses Spring Boot version 3.4.5.

## 4.3 Spring AI

Spring AI is used to connect the application with the AI service. The project uses the Spring AI OpenAI-compatible model starter.

## 4.4 Groq

Groq is configured as the AI service provider. The application uses an OpenAI-compatible API endpoint and obtains the API key from an environment variable.

The configured default model is:

`openai/gpt-oss-120b`

The model temperature is configured as `0.1`.

## 4.5 HTML, CSS and JavaScript

The frontend is built using HTML, CSS, and JavaScript. It is served as static content from the Spring Boot application.

JavaScript is used to communicate with the backend REST endpoints and to update the interface without reloading the page.

## 4.6 Maven

Maven is used for dependency management and project building.

## 4.7 Docker

A Dockerfile is included in the project. It uses Eclipse Temurin Java 17 images and builds the application using Maven.

---

# Chapter 5: System Architecture

InstantAPI follows a layered backend structure.

The main parts of the system are:

1. Frontend
2. REST Controllers
3. Service Layer
4. AI Service
5. Generator Service
6. Generator Components
7. Template Service
8. Generated Project

The frontend sends requests to the backend. Controllers receive these requests and pass them to the appropriate service.

The AI service handles natural-language understanding and explanations. The generator service validates the input and starts project generation.

The generator components create individual Java files. The TemplateService provides common template handling and variable replacement.

Finally, the generated files are placed into a project directory and compressed into a ZIP file.

## 5.1 Architecture Flow

The main AI generation flow is:

**User → Web Interface → AI Controller → AI Service → AI Model → Generator Request → Editable Preview → Generator Controller → Generator Service → Project Generator → ZIP File**

For manual generation, the AI step is skipped:

**User → Web Interface → Generator Controller → Generator Service → Project Generator → ZIP File**

---

# Chapter 6: Frontend Design

The frontend provides a simple interface for API generation.

The interface contains two main modes:

* AI mode
* Manual mode

In AI mode, the user enters a description of the required API. The frontend sends this description to:

`POST /api/ai/understand`

The response contains the service name and parameters. These values are shown to the user for review.

In manual mode, the user can enter the service name and add fields manually.

The interface also provides an Add Field option and allows fields to be removed.

After the information is ready, the Generate Project button sends the data to:

`POST /api/generate`

The generated ZIP file is then downloaded by the browser.

The interface also contains an AI explanation area. After project generation, the user can ask a question about the generated API.

---

# Chapter 7: Backend Design

## 7.1 Main Application

The main application class is:

`InstantApiApplication`

It uses the Spring Boot `@SpringBootApplication` annotation and starts the application using SpringApplication.

## 7.2 Controllers

The project contains two main controllers.

### AiController

The AiController handles AI-related requests.

It provides:

`POST /api/ai/understand`

This endpoint converts the user's description into a GeneratorRequest.

It also provides:

`POST /api/ai/explain`

This endpoint sends a question about the generated API to the AI service.

### GeneratorController

The GeneratorController handles project generation.

It provides:

`POST /api/generate`

This endpoint receives the GeneratorRequest, starts the generator, and returns the generated ZIP file.

## 7.3 Service Layer

The InstantAPI source follows the same layered convention as the projects it generates.

The `services` package contains the service interfaces:

* `AiService`
* `GeneratorService`

The `servicesImpl` package contains the corresponding implementations:

* `AiServiceImpl`
* `GeneratorServiceImpl`

The `exception` package contains the application-wide error handling:

* `GlobalExceptionHandler`

The `dto` package contains the request and response classes, and the `generator` package contains the generator components and the TemplateService.

---

# Chapter 8: AI Integration

AI is one of the main parts of InstantAPI.

The AI service has two main functions.

First, it understands a user's API description. The system gives the AI model specific instructions about the expected JSON structure.

The response contains a service name and a list of parameters.

The system also tells the model which data types are supported. These include String, Integer, Long, Double, Float, Boolean, and LocalDate.

The system always expects an ID field. If the user does not provide fields and only gives the name of an entity, the AI is instructed to infer three to five common fields.

The second AI function is explanation. The system sends the generated API name, its fields, and the user's question to the AI model. The returned answer is shown to the user.

## 8.1 Role of AI in the System

AI is not responsible for generating the complete Java project.

Instead, AI is used mainly for understanding natural-language requirements and answering questions.

The actual source-code generation is performed by Java generator classes and templates. This gives the system more control over the final structure.

This is a useful design choice because the generated files follow predefined rules instead of depending completely on free-form AI-generated source code.

---

# Chapter 9: Project Generation

The ProjectGenerator is responsible for creating the generated project.

When generation starts, the system converts the service name into a suitable Java class name and package name.

For example, a service name such as `Student` can produce a package based on:

`com.instantapi.student`

The generator then creates directories for:

* entity
* dto
* transfer
* repository
* services
* servicesImpl
* controller
* payload
* exception
* helper

The system also creates the Maven `pom.xml` and `application.properties`.

## 9.1 Generated Classes

The generated project contains an application class, entity, DTO, transformer, repository, service, service implementation, controller, and supporting classes.

This gives the generated API a consistent structure.

## 9.2 Template-Based Generation

The system uses templates stored inside the application resources.

The TemplateService reads these templates and replaces variables such as:

* package
* class name
* field name
* table name
* artifact ID
* ID field

This method reduces the need to manually build every Java source file as a long string.

---

# Chapter 10: CRUD API Generation

The generated projects provide basic CRUD operations.

The generated REST API includes:

| Method      | Purpose               |
| ----------- | --------------------- |
| POST        | Create a record       |
| GET         | Get a list of records |
| GET with ID | Get one record        |
| PUT         | Update a record       |
| DELETE      | Delete a record       |

The list operation also supports pagination parameters.

The generated structure uses a repository and service layer so that the controller does not contain all application logic.

---

# Chapter 11: Data Validation and Processing

Before project generation, GeneratorServiceImpl checks the received request.

The service name must be provided. If it is missing, an exception is raised.

If the parameter list is missing, an empty list is created.

Fields without a name are ignored. If a field does not have a data type, the system uses String as the default type.

The system also checks whether an ID field exists. If it does not exist, a String ID field is added automatically.

This processing helps keep the input in a form that the generator can use.

---

# Chapter 12: Generated Project Structure

A generated API follows a layered structure similar to:

```text
src/main/java/com/instantapi/<name>/
    entity/
    dto/
    transfer/
    repository/
    services/
    servicesImpl/
    controller/
    payload/
    exception/
    helper/
```

The entity represents the main data object.

The DTO is used for transferring data between the application layers.

The transformer converts data between DTO and entity objects.

The repository provides the data access layer.

The service defines application operations.

The service implementation contains the main service logic.

The controller provides REST endpoints.

The exception package provides error handling.

The helper and payload classes support response and pagination handling.

---

# Chapter 13: Error Handling

InstantAPI contains a global exception handler in the generated projects.

The generated structure includes exceptions such as:

* ResourceNotFoundException
* BadApiException
* GlobalExceptionHandler

This provides a common location for handling application errors.

The system also performs basic input checking before project generation. For example, an empty service name is rejected.

The error handling in the current project is mainly focused on the standard errors required by the generated CRUD application.

---

# Chapter 14: API Endpoints of InstantAPI

The main InstantAPI endpoints are:

| Method | Endpoint             | Function                             |
| ------ | -------------------- | ------------------------------------ |
| POST   | `/api/ai/understand` | Understand an API description        |
| POST   | `/api/ai/explain`    | Answer questions about an API        |
| POST   | `/api/generate`      | Generate and download an API project |

The `/api/ai/understand` endpoint receives an AI request.

The `/api/ai/explain` endpoint receives a question together with information about the generated API.

The `/api/generate` endpoint receives the service name and parameters and returns the generated project as a downloadable file.

---

# Chapter 15: Deployment

The project contains deployment configuration for container and cloud-based deployment.

A Dockerfile is included. The build stage uses Java 17 and Maven to create the application JAR file. The final stage uses a Java 17 runtime image.

The container listens on port 8080 by default and also respects the `PORT` environment variable used by cloud platforms.

The project also contains configuration for Render and Fly.io, plus a deployment script for Oracle Cloud Free Tier.

Environment variables are used for important configuration values such as the Groq API key, base URL, and model.

Using an environment variable for the AI API key is better than placing the actual key directly in the source code.

---

# Chapter 16: Testing and Evaluation

The provided project contains no files under the standard Maven test directory. Therefore, a complete automated testing result cannot be claimed from the supplied source code.

The project does contain several generated example APIs, including:

* BookAPI
* CarAPI
* HostelAPI
* ProductAPI
* StudentAPI
* TestAPI

These generated projects show that the generator has been used to create different API structures.

However, these generated files alone are not enough to claim a formal performance evaluation or complete functional testing.

A stronger evaluation would require documented test cases and their results.

Possible tests for future evaluation include:

1. Testing AI understanding with different API descriptions.
2. Testing manual API generation.
3. Testing missing service names.
4. Testing missing field types.
5. Testing automatic ID creation.
6. Testing field editing.
7. Testing generated CRUD operations.
8. Testing invalid API requests.
9. Testing generated project compilation.
10. Testing ZIP file generation and download.

---

# Chapter 17: Limitations

The current implementation has several limitations.

First, the generator supports a limited number of data types. More advanced types are not directly supported.

Second, the AI interpretation depends on the configured AI service. An incorrect or incomplete AI response may affect the generated field list.

Third, the project generator is designed around a fixed Spring Boot structure. It is not intended to generate every possible type of REST application.

Fourth, the current implementation does not provide evidence of a complete automated testing suite.

The system also does not provide a full database configuration or a complete production security system as part of the main InstantAPI application.

These limitations do not prevent the system from performing its main purpose, but they define the current boundaries of the project.

---

# Chapter 18: Future Improvements

Several improvements can be made in future versions.

The supported data types can be increased to include more Java and database types.

Authentication and authorization can also be added to generated APIs. This would make the generated projects more suitable for applications that require user accounts and access control.

Database configuration could be made configurable from the user interface.

More generation options could also be provided. For example, users could select database type, package name, API version, and additional project dependencies.

Automated testing can be added to the generator so that every generated project includes unit and integration tests.

The AI system could also be improved to provide better handling of complex API descriptions.

Another possible improvement is adding OpenAPI or Swagger documentation to generated projects.

---

# Chapter 19: Discussion

InstantAPI combines two different approaches: AI-based requirement understanding and rule-based source-code generation.

The AI part makes the input process easier because users can describe their requirements using normal language. At the same time, the Java generator controls the final project structure.

This combination is more controlled than asking an AI model to generate the complete project directly. The generator uses predefined templates and Java classes, so the main structure remains consistent.

The editable preview is also useful because AI output is not always guaranteed to match the user's exact requirement. Allowing the user to modify the fields gives the user control before the source code is generated.

The main value of the system is therefore not only the use of AI. It is the combination of natural-language input, user review, deterministic code generation, and automatic project packaging.

---

# Chapter 20: Conclusion

InstantAPI is a web-based tool for generating basic Spring Boot CRUD REST API projects. It reduces some of the repeated work involved in creating standard API structures.

The system provides AI mode and manual mode. AI mode converts a natural-language description into structured API information. The user can then review and modify the generated fields.

After the input is confirmed, Java generator components create the project files using predefined templates. The generated project follows a layered structure containing entity, DTO, transformer, repository, service, service implementation, controller, exception handling, and supporting classes.

The project also provides an AI explanation feature and supports packaging the generated application into a ZIP file.

Based on the supplied implementation, the project successfully defines a clear approach for combining AI-assisted requirement understanding with template-based software generation. Its current limitations are mainly related to supported data types, fixed generation rules, and the absence of a documented automated testing suite.

Future development can extend the generator with more database options, authentication, testing, API documentation, additional data types, and more advanced project configuration.

Overall, InstantAPI provides a practical foundation for reducing repetitive work in basic Spring Boot REST API development while keeping the generated source code under controlled templates and rules.
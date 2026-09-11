# InstantAPI

Describe your API in plain English and download a complete, ready-to-run Spring Boot CRUD project.

InstantAPI uses AI (Groq, via Spring AI) to turn a natural-language description into entity fields, then generates the source files with deterministic Java generators using a clean layered pattern (Entity, DTO, Transformer, Repository, Service, ServiceImpl, Controller, exception handling, pagination).

## Features

- **AI mode** — type something like `Create an API for products with name, price and stock` and InstantAPI infers the fields for you.
- **Manual mode** — define the service name and fields yourself.
- **Editable preview** — tweak, add or remove AI-generated fields before generating.
- **Editable fields** — AI may miss fields; add them manually after generation.
- **Full CRUD project** — generated ZIP contains everything needed to run (`mvn spring-boot:run`).

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/ai/understand` | Convert a prompt into a `GeneratorRequest` |
| POST | `/api/ai/explain` | Ask questions about the generated API |
| POST | `/api/generate` | Generate and download the project ZIP |

### `/api/generate` example

```json
{
  "serviceName": "Student",
  "parameters": [
    { "name": "id", "dataType": "String" },
    { "name": "firstName", "dataType": "String" },
    { "name": "email", "dataType": "String" },
    { "name": "dateOfBirth", "dataType": "LocalDate" }
  ]
}
```

Supported data types: `String`, `Integer`, `Long`, `Double`, `Float`, `Boolean`, `LocalDate`.

## Generated project structure

Every generated API follows a consistent layered CRUD pattern:

```
src/main/java/com/instantapi/<name>/
├── entity/        JPA entity (Lombok, String UUID id)
├── dto/           Request/response DTO
├── transfer/      Transformer interface + implementation
├── repository/    Spring Data repository
├── services/      Service interface
├── servicesImpl/  Service implementation (pagination, UUID ids)
├── controller/    REST controller
├── payload/       ApiResponseMessage, PageableResponse
├── exception/     ResourceNotFoundException, GlobalExceptionHandler
└── helper/        PageableResponse building helper
```

Generated REST endpoints:

```
POST   /{name}s          create
GET    /{name}s          list (paginated: ?pageSize&pageNo&sortDir&sortedBy)
GET    /{name}s/{id}     find one
PUT    /{name}s/{id}     update
DELETE /{name}s/{id}     delete
```

## Running InstantAPI itself

```powershell
$env:GROQ_API_KEY="your-groq-key"
mvn spring-boot:run
```

Then open `http://localhost:8080`. Java 17+ and Maven are required.

## Configuration

| Property | Env var | Default |
|----------|---------|---------|
| `spring.ai.openai.base-url` | `GROQ_BASE_URL` | `https://api.groq.com/openai` |
| `spring.ai.openai.api-key` | `GROQ_API_KEY` | *(required for AI mode)* |
| `spring.ai.openai.chat.options.model` | `GROQ_MODEL` | `openai/gpt-oss-120b` |
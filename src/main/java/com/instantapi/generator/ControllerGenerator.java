package com.instantapi.generator;

import org.springframework.stereotype.Component;

@Component
public class ControllerGenerator {
    public String generate(String className, String packageName) {
        String field = NameUtil.fieldName(className);
        String dto = className + "DTO";
        return "package " + packageName + ".controller;\n\n" +
                "import " + packageName + ".dto." + dto + ";\n" +
                "import " + packageName + ".payload.ApiResponseMessage;\n" +
                "import " + packageName + ".payload.PageableResponse;\n" +
                "import " + packageName + ".services." + className + "Service;\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.annotation.*;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/" + field + "s\")\n" +
                "public class " + className + "Controller {\n\n" +
                "    private final " + className + "Service " + field + "Service;\n\n" +
                "    public " + className + "Controller(" + className + "Service " + field + "Service) {\n" +
                "        this." + field + "Service = " + field + "Service;\n" +
                "    }\n\n" +
                "    @PostMapping()\n" +
                "    public ResponseEntity<ApiResponseMessage> save(@RequestBody " + dto + " " + field + "Dto) {\n" +
                "        " + dto + " saved = " + field + "Service.save(" + field + "Dto);\n" +
                "        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()\n" +
                "                .message(\"" + className + " Created Successfully!\")\n" +
                "                .data(saved)\n" +
                "                .status(HttpStatus.CREATED)\n" +
                "                .success(true)\n" +
                "                .build();\n" +
                "        return new ResponseEntity<>(apiResponseMessage, HttpStatus.CREATED);\n" +
                "    }\n\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public ResponseEntity<ApiResponseMessage> update(@PathVariable String id, @RequestBody " + dto + " " + field + "Dto) {\n" +
                "        " + dto + " updated = " + field + "Service.update(id, " + field + "Dto);\n" +
                "        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()\n" +
                "                .message(\"" + className + " Updated Successfully!\")\n" +
                "                .data(updated)\n" +
                "                .status(HttpStatus.OK)\n" +
                "                .success(true)\n" +
                "                .build();\n" +
                "        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);\n" +
                "    }\n\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public ResponseEntity<" + dto + "> getById(@PathVariable String id) {\n" +
                "        return new ResponseEntity<>(" + field + "Service.getById(id), HttpStatus.OK);\n" +
                "    }\n\n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<PageableResponse<" + dto + ">> getAll(\n" +
                "            @RequestParam(value = \"pageSize\", defaultValue = \"10\", required = false) int pageSize,\n" +
                "            @RequestParam(value = \"pageNo\", defaultValue = \"0\", required = false) int pageNo,\n" +
                "            @RequestParam(value = \"sortDir\", defaultValue = \"dec\", required = false) String sortDir,\n" +
                "            @RequestParam(value = \"sortedBy\", defaultValue = \"" + field + "Id\", required = false) String sortedBy) {\n" +
                "        return new ResponseEntity<>(" + field + "Service.getAll(pageSize, pageNo, sortDir, sortedBy), HttpStatus.OK);\n" +
                "    }\n\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String id) {\n" +
                "        " + field + "Service.delete(id);\n" +
                "        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()\n" +
                "                .message(\"" + className + " Deleted Successfully!\")\n" +
                "                .status(HttpStatus.OK)\n" +
                "                .success(true)\n" +
                "                .data(null)\n" +
                "                .build();\n" +
                "        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);\n" +
                "    }\n" +
                "}\n";
    }
}
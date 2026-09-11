package {{package}}.controller;

import {{package}}.dto.{{className}}DTO;
import {{package}}.payload.ApiResponseMessage;
import {{package}}.payload.PageableResponse;
import {{package}}.services.{{className}}Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{{fieldNamePlural}}")
public class {{className}}Controller {

    private final {{className}}Service {{fieldName}}Service;

    public {{className}}Controller({{className}}Service {{fieldName}}Service) {
        this.{{fieldName}}Service = {{fieldName}}Service;
    }

    @PostMapping()
    public ResponseEntity<ApiResponseMessage> save(@RequestBody {{className}}DTO {{fieldName}}Dto) {
        {{className}}DTO saved = {{fieldName}}Service.save({{fieldName}}Dto);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("{{className}} Created Successfully!")
                .data(saved)
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> update(@PathVariable String id, @RequestBody {{className}}DTO {{fieldName}}Dto) {
        {{className}}DTO updated = {{fieldName}}Service.update(id, {{fieldName}}Dto);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("{{className}} Updated Successfully!")
                .data(updated)
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<{{className}}DTO> getById(@PathVariable String id) {
        return new ResponseEntity<>({{fieldName}}Service.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<{{className}}DTO>> getAll(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "sortDir", defaultValue = "dec", required = false) String sortDir,
            @RequestParam(value = "sortedBy", defaultValue = "{{idField}}", required = false) String sortedBy) {
        return new ResponseEntity<>({{fieldName}}Service.getAll(pageSize, pageNo, sortDir, sortedBy), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String id) {
        {{fieldName}}Service.delete(id);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("{{className}} Deleted Successfully!")
                .status(HttpStatus.OK)
                .success(true)
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }
}

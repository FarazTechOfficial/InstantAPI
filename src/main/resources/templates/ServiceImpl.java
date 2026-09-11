package {{package}}.servicesImpl;

import {{package}}.dto.{{className}}DTO;
import {{package}}.entity.{{className}};
import {{package}}.exception.ResourceNotFoundException;
import {{package}}.helper.Helper;
import {{package}}.payload.PageableResponse;
import {{package}}.repository.{{className}}Repository;
import {{package}}.services.{{className}}Service;
import {{package}}.transfer.{{className}}Transformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class {{className}}ServiceImpl implements {{className}}Service {

    private final {{className}}Repository {{fieldName}}Repository;
    private final {{className}}Transformer {{fieldName}}Transformer;

    public {{className}}ServiceImpl({{className}}Repository {{fieldName}}Repository, {{className}}Transformer {{fieldName}}Transformer) {
        this.{{fieldName}}Repository = {{fieldName}}Repository;
        this.{{fieldName}}Transformer = {{fieldName}}Transformer;
    }

    @Override
    public {{className}}DTO save({{className}}DTO dto) {
        // new record, give it a fresh id
        {{className}} {{fieldName}} = {{fieldName}}Transformer.toEntity(dto);
        {{fieldName}}.set{{idFieldCap}}(UUID.randomUUID().toString());
        return {{fieldName}}Transformer.toDto({{fieldName}}Repository.save({{fieldName}}));
    }

    @Override
    public PageableResponse<{{className}}DTO> getAll(int pageSize, int pageNo, String sortDir, String sortedBy) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortedBy).descending() : Sort.by(sortedBy).ascending();
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        Page<{{className}}> all = {{fieldName}}Repository.findAll(pageRequest);
        return Helper.getPageableResponse(all, {{fieldName}}Transformer);
    }

    @Override
    public {{className}}DTO getById(String id) {
        {{className}} {{fieldName}} = {{fieldName}}Repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException({{notFoundMessage}}));
        return {{fieldName}}Transformer.toDto({{fieldName}});
    }

    @Override
    public {{className}}DTO update(String id, {{className}}DTO dto) {
        {{className}} {{fieldName}} = {{fieldName}}Repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException({{notFoundMessage}}));
        {{fieldName}}Transformer.toUpdate({{fieldName}}, dto);
        return {{fieldName}}Transformer.toDto({{fieldName}}Repository.save({{fieldName}}));
    }

    @Override
    public void delete(String id) {
        {{fieldName}}Repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException({{notFoundMessage}}));
        {{fieldName}}Repository.deleteById(id);
    }
}

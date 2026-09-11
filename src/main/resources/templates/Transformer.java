package {{package}}.transfer;

import {{package}}.dto.{{className}}DTO;
import {{package}}.entity.{{className}};
{{imports}}
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class {{className}}Transformer implements Transformer<{{className}}, {{className}}DTO> {

    @Override
    public {{className}} toEntity({{className}}DTO dto) {
        {{className}} entity = new {{className}}();
        entity.set{{idFieldCap}}(dto.get{{idFieldCap}}());
{{toEntityBody}}
        return entity;
    }

    @Override
    public {{className}}DTO toDto({{className}} entity) {
        {{className}}DTO dto = new {{className}}DTO();
        dto.set{{idFieldCap}}(entity.get{{idFieldCap}}());
{{toDtoBody}}
        return dto;
    }

    @Override
    public {{className}} toUpdate({{className}} entity, {{className}}DTO dto) {
        if (dto.get{{idFieldCap}}() != null) entity.set{{idFieldCap}}(dto.get{{idFieldCap}}());
{{toUpdateBody}}
        return entity;
    }

    @Override
    public List<{{className}}DTO> toDtoList(List<{{className}}> entities) {
        List<{{className}}DTO> list = new ArrayList<>();
        for ({{className}} entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }

    @Override
    public List<{{className}}> toEntityList(List<{{className}}DTO> dtos) {
        List<{{className}}> list = new ArrayList<>();
        for ({{className}}DTO dto : dtos) {
            list.add(toEntity(dto));
        }
        return list;
    }
}

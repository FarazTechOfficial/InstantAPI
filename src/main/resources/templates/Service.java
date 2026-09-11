package {{package}}.services;

import {{package}}.dto.{{className}}DTO;
import {{package}}.payload.PageableResponse;

public interface {{className}}Service {

    {{className}}DTO save({{className}}DTO dto);

    PageableResponse<{{className}}DTO> getAll(int pageSize, int pageNo, String sortDir, String sortedBy);

    {{className}}DTO getById(String id);

    {{className}}DTO update(String id, {{className}}DTO dto);

    void delete(String id);
}

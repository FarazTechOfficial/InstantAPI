package {{package}}.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
{{imports}}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class {{className}}DTO {

    private String {{idField}};
{{fieldDeclarations}}
}

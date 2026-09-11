package {{package}}.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
{{imports}}

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "{{tableName}}")
public class {{className}} {

    @Id
    private String {{idField}};
{{fieldDeclarations}}
}

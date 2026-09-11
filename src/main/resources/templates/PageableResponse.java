package {{package}}.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse<D> {
    private List<D> data;
    private int pageSize;
    private int totalElements;
    private int pageNo;
    private int totalPage;
    private boolean lastPage;
}

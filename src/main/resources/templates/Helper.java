package {{package}}.helper;

import {{package}}.payload.PageableResponse;
import {{package}}.transfer.Transformer;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static <E, D> PageableResponse<D> getPageableResponse(Page<E> page, Transformer<E, D> transformer) {
        List<D> data = new ArrayList<>();
        for (E entity : page.getContent()) {
            data.add(transformer.toDto(entity));
        }
        PageableResponse<D> pageableResponse = new PageableResponse<>();
        pageableResponse.setData(data);
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setLastPage(page.isLast());
        pageableResponse.setTotalElements((int) page.getTotalElements());
        pageableResponse.setPageNo(page.getNumber());
        pageableResponse.setTotalPage(page.getTotalPages());
        return pageableResponse;
    }
}

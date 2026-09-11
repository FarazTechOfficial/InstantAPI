package {{package}}.transfer;

import java.util.List;

public interface Transformer<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
    E toUpdate(E entity, D dto);
    List<D> toDtoList(List<E> entities);
    List<E> toEntityList(List<D> dtos);
}

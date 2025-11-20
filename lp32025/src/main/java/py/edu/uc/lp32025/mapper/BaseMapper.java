package py.edu.uc.lp32025.mapper;

public interface BaseMapper<E, D> {
    D toDto(E entity);

    default E toEntity(D dto) {
        throw new UnsupportedOperationException("Conversión no implementada.");
    }
}

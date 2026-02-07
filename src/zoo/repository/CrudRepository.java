package zoo.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface CrudRepository<T, ID> {

    T create(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean update(T entity);

    boolean delete(ID id);

    // ✅ Default method: exists
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    // ✅ Default method: getRequired (то, чего не хватает по ошибке)
    default T getRequired(ID id, Supplier<? extends RuntimeException> exceptionSupplier) {
        return findById(id).orElseThrow(exceptionSupplier);
    }

    // ✅ Static method: requirePositiveInt (то, чего не хватает по ошибке)
    static void requirePositiveInt(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0");
        }
    }
}
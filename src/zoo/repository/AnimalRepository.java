package zoo.repository;

import zoo.domain.Animal;
import java.util.List;

public interface AnimalRepository extends CrudRepository<Animal, Integer> {
    List<Animal> findByZooId(int zooId);
}
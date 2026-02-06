package zoo.repository;

import zoo.domain.Zookeeper;
import java.util.List;

public interface ZookeeperRepository extends CrudRepository<Zookeeper, Integer> {
    List<Zookeeper> findByZooId(int zooId);
}
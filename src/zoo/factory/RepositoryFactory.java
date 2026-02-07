package zoo.factory;

import zoo.repository.AnimalRepository;
import zoo.repository.ZooRepository;
import zoo.repository.ZookeeperRepository;
import zoo.repository.jdbc.JdbcAnimalRepository;
import zoo.repository.jdbc.JdbcZooRepository;
import zoo.repository.jdbc.JdbcZookeeperRepository;

public final class RepositoryFactory {

    private RepositoryFactory() {}

    public static ZooRepository zooRepository() {
        return new JdbcZooRepository();
    }

    public static AnimalRepository animalRepository() {
        return new JdbcAnimalRepository();
    }

    public static ZookeeperRepository zookeeperRepository() {
        return new JdbcZookeeperRepository();
    }
}
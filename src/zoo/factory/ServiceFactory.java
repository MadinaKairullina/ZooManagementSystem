package zoo.factory;

import zoo.repository.AnimalRepository;
import zoo.repository.ZooRepository;
import zoo.repository.ZookeeperRepository;
import zoo.service.AnimalService;
import zoo.service.ZooService;
import zoo.service.ZookeeperService;

public final class ServiceFactory {

    private ServiceFactory() {}

    public static ZooService zooService(ZooRepository zooRepo) {
        return new ZooService(zooRepo);
    }

    public static AnimalService animalService(AnimalRepository animalRepo, ZooRepository zooRepo) {
        return new AnimalService(animalRepo, zooRepo);
    }

    public static ZookeeperService zookeeperService(ZookeeperRepository keeperRepo, ZooRepository zooRepo) {
        return new ZookeeperService(keeperRepo, zooRepo);
    }
}
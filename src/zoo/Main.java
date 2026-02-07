package zoo;

import zoo.factory.RepositoryFactory;
import zoo.factory.ServiceFactory;
import zoo.repository.AnimalRepository;
import zoo.repository.ZooRepository;
import zoo.repository.ZookeeperRepository;
import zoo.service.AnimalService;
import zoo.service.ZooService;
import zoo.service.ZookeeperService;
import zoo.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        try {
            ZooRepository zooRepo = RepositoryFactory.zooRepository();
            AnimalRepository animalRepo = RepositoryFactory.animalRepository();
            ZookeeperRepository zookeeperRepo = RepositoryFactory.zookeeperRepository();

            ZooService zooService = ServiceFactory.zooService(zooRepo);
            AnimalService animalService = ServiceFactory.animalService(animalRepo, zooRepo);
            ZookeeperService zookeeperService = ServiceFactory.zookeeperService(zookeeperRepo, zooRepo);

            new ConsoleMenu(zooService, animalService, zookeeperService).run();
        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
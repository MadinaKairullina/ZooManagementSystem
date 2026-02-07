package zoo.service;

import zoo.domain.Zookeeper;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.CrudRepository;
import zoo.repository.ZooRepository;
import zoo.repository.ZookeeperRepository;

import java.util.List;

public class ZookeeperService {

    private final ZookeeperRepository repo;
    private final ZooRepository zooRepo;

    public ZookeeperService(ZookeeperRepository repo, ZooRepository zooRepo) {
        this.repo = repo;
        this.zooRepo = zooRepo;
    }

    public List<Zookeeper> getAll() {
        return repo.findAll();
    }

    public List<Zookeeper> getByZooId(int zooId) {
        requirePositive(zooId, "zooId");
        ensureZooExists(zooId);
        return repo.findByZooId(zooId);
    }

    public Zookeeper getById(int id) {
        requirePositive(id, "Zookeeper id");
        return repo.getRequired(id, () -> new NotFoundException("Zookeeper id=" + id + " not found"));
    }

    public Zookeeper add(String name, int experienceYears, int zooId) {
        validateZookeeperFields(name, experienceYears, zooId);
        ensureZooExists(zooId);

        return repo.create(
                Zookeeper.builder()
                        .name(name)
                        .experienceYears(experienceYears)
                        .zooId(zooId)
                        .build()
        );
    }

    public void update(int id, String name, int experienceYears, int zooId) {
        requirePositive(id, "Zookeeper id");
        validateZookeeperFields(name, experienceYears, zooId);
        ensureZooExists(zooId);

        boolean ok = repo.update(
                Zookeeper.builder()
                        .id(id)
                        .name(name)
                        .experienceYears(experienceYears)
                        .zooId(zooId)
                        .build()
        );

        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    public void updateExperience(int id, int experienceYears) {
        requirePositive(id, "Zookeeper id");
        if (experienceYears < 0) throw new ValidationException("Experience years cannot be negative");

        Zookeeper current = getById(id);

        boolean ok = repo.update(
                Zookeeper.builder()
                        .id(current.getId())
                        .name(current.getName())
                        .experienceYears(experienceYears)
                        .zooId(current.getZooId())
                        .build()
        );

        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    public void delete(int id) {
        requirePositive(id, "Zookeeper id");
        boolean ok = repo.delete(id);
        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    private void ensureZooExists(int zooId) {
        zooRepo.getRequired(zooId, () -> new NotFoundException("Zoo id=" + zooId + " not found"));
    }

    private void validateZookeeperFields(String name, int experienceYears, int zooId) {
        if (name == null || name.trim().isEmpty())
            throw new ValidationException("Zookeeper name cannot be empty");
        if (experienceYears < 0)
            throw new ValidationException("Experience years cannot be negative");
        requirePositive(zooId, "zooId");
    }

    private void requirePositive(int value, String fieldName) {
        try {
            CrudRepository.requirePositiveInt(value, fieldName);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
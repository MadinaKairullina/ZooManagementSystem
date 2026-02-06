package zoo.service;

import zoo.domain.Zookeeper;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.ZookeeperRepository;

import java.util.List;

public class ZookeeperService {

    private final ZookeeperRepository repo;

    public ZookeeperService(ZookeeperRepository repo) {
        this.repo = repo;
    }

    public List<Zookeeper> getAll() {
        return repo.findAll();
    }

    public List<Zookeeper> getByZooId(int zooId) {
        if (zooId <= 0) throw new ValidationException("zooId must be > 0");
        return repo.findByZooId(zooId);
    }

    public Zookeeper getById(int id) {
        if (id <= 0) throw new ValidationException("Zookeeper id must be > 0");
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Zookeeper id=" + id + " not found"));
    }

    public Zookeeper add(String name, int experienceYears, int zooId) {
        validateZookeeperFields(name, experienceYears, zooId);
        Zookeeper created = repo.create(new Zookeeper(0, name, experienceYears, zooId));
        return created;
    }

    public void update(int id, String name, int experienceYears, int zooId) {
        if (id <= 0) throw new ValidationException("Zookeeper id must be > 0");
        validateZookeeperFields(name, experienceYears, zooId);

        boolean ok = repo.update(new Zookeeper(id, name, experienceYears, zooId));
        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    public void updateExperience(int id, int experienceYears) {
        if (id <= 0) throw new ValidationException("Zookeeper id must be > 0");
        if (experienceYears < 0) throw new ValidationException("Experience years cannot be negative");

        Zookeeper current = getById(id);
        Zookeeper updated = new Zookeeper(current.getId(), current.getName(), experienceYears, current.getZooId());

        boolean ok = repo.update(updated);
        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    public void delete(int id) {
        if (id <= 0) throw new ValidationException("Zookeeper id must be > 0");
        boolean ok = repo.delete(id);
        if (!ok) throw new NotFoundException("Zookeeper id=" + id + " not found");
    }

    private void validateZookeeperFields(String name, int experienceYears, int zooId) {
        if (name == null || name.trim().isEmpty())
            throw new ValidationException("Zookeeper name cannot be empty");
        if (experienceYears < 0)
            throw new ValidationException("Experience years cannot be negative");
        if (zooId <= 0)
            throw new ValidationException("zooId must be > 0");
    }
}

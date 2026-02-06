package zoo.service;

import zoo.domain.Zoo;
import zoo.exception.NotFoundException;
import zoo.repository.ZooRepository;

import java.util.List;

public class ZooService {
    private final ZooRepository repo;

    public ZooService(ZooRepository repo) {
        this.repo = repo;
    }

    public List<Zoo> getAll() {
        return repo.findAll();
    }

    public Zoo getById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Zoo id=" + id + " not found"));
    }

    public Zoo add(Zoo zoo) {
        return repo.create(zoo);
    }

    public void update(Zoo zoo) {
        if (!repo.update(zoo))
            throw new NotFoundException("Zoo id=" + zoo.getId() + " not found");
    }

    public void delete(int id) {
        if (!repo.delete(id))
            throw new NotFoundException("Zoo id=" + id + " not found");
    }
}
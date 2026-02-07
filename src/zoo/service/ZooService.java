package zoo.service;

import zoo.domain.Zoo;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.CrudRepository;
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
        requirePositive(id, "Zoo id");
        return repo.getRequired(id, () -> new NotFoundException("Zoo id=" + id + " not found"));
    }

    public Zoo add(String name, String city) {
        validateZooFields(name, city);

        return repo.create(
                Zoo.builder()
                        .name(name)
                        .city(city)
                        .build()
        );
    }

    // Можно оставить для совместимости, если где-то ещё передаёшь Zoo напрямую
    public Zoo add(Zoo zoo) {
        if (zoo == null) throw new ValidationException("Zoo cannot be null");
        return repo.create(zoo);
    }

    public void update(int id, String name, String city) {
        requirePositive(id, "Zoo id");
        validateZooFields(name, city);

        boolean ok = repo.update(
                Zoo.builder()
                        .id(id)
                        .name(name)
                        .city(city)
                        .build()
        );

        if (!ok) throw new NotFoundException("Zoo id=" + id + " not found");
    }

    // Можно оставить для совместимости
    public void update(Zoo zoo) {
        if (zoo == null) throw new ValidationException("Zoo cannot be null");
        requirePositive(zoo.getId(), "Zoo id");

        boolean ok = repo.update(zoo);
        if (!ok) throw new NotFoundException("Zoo id=" + zoo.getId() + " not found");
    }

    public void delete(int id) {
        requirePositive(id, "Zoo id");
        boolean ok = repo.delete(id);
        if (!ok) throw new NotFoundException("Zoo id=" + id + " not found");
    }

    private void validateZooFields(String name, String city) {
        if (name == null || name.trim().isEmpty())
            throw new ValidationException("Zoo name cannot be empty");
        if (city == null || city.trim().isEmpty())
            throw new ValidationException("City cannot be empty");
    }

    private void requirePositive(int value, String fieldName) {
        try {
            CrudRepository.requirePositiveInt(value, fieldName);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
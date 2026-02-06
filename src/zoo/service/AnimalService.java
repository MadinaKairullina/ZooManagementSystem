package zoo.service;

import zoo.domain.Animal;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.AnimalRepository;

import java.util.List;

public class AnimalService {

    private final AnimalRepository repo;

    public AnimalService(AnimalRepository repo) {
        this.repo = repo;
    }

    public List<Animal> getAll() {
        return repo.findAll();
    }

    public List<Animal> getByZooId(int zooId) {
        if (zooId <= 0) throw new ValidationException("zooId must be > 0");
        return repo.findByZooId(zooId);
    }

    public Animal getById(int id) {
        if (id <= 0) throw new ValidationException("Animal id must be > 0");
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal id=" + id + " not found"));
    }

    public Animal add(String name, String species, int age, int zooId) {
        validateAnimalFields(name, species, age, zooId);
        // id=0 (или любой) — БД присвоит id сама (SERIAL/IDENTITY)
        Animal created = repo.create(new Animal(0, name, species, age, zooId));
        return created;
    }

    public void update(int id, String name, String species, int age, int zooId) {
        if (id <= 0) throw new ValidationException("Animal id must be > 0");
        validateAnimalFields(name, species, age, zooId);

        boolean ok = repo.update(new Animal(id, name, species, age, zooId));
        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    public void updateAge(int id, int newAge) {
        if (id <= 0) throw new ValidationException("Animal id must be > 0");
        if (newAge < 0) throw new ValidationException("Age cannot be negative");

        Animal current = getById(id); // тут NotFoundException, если нет
        Animal updated = new Animal(current.getId(), current.getName(), current.getSpecies(), newAge, current.getZooId());

        boolean ok = repo.update(updated);
        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    public void delete(int id) {
        if (id <= 0) throw new ValidationException("Animal id must be > 0");
        boolean ok = repo.delete(id);
        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    private void validateAnimalFields(String name, String species, int age, int zooId) {
        if (name == null || name.trim().isEmpty())
            throw new ValidationException("Animal name cannot be empty");
        if (species == null || species.trim().isEmpty())
            throw new ValidationException("Species cannot be empty");
        if (age < 0)
            throw new ValidationException("Age cannot be negative");
        if (zooId <= 0)
            throw new ValidationException("zooId must be > 0");
    }
}
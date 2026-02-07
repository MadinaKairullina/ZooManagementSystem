package zoo.service;

import zoo.domain.Animal;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.AnimalRepository;
import zoo.repository.CrudRepository;
import zoo.repository.ZooRepository;

import java.util.List;

public class AnimalService {

    private final AnimalRepository repo;
    private final ZooRepository zooRepo;

    public AnimalService(AnimalRepository repo, ZooRepository zooRepo) {
        this.repo = repo;
        this.zooRepo = zooRepo;
    }

    public List<Animal> getAll() {
        return repo.findAll();
    }

    public List<Animal> getByZooId(int zooId) {
        requirePositive(zooId, "zooId");
        ensureZooExists(zooId);
        return repo.findByZooId(zooId);
    }

    public Animal getById(int id) {
        requirePositive(id, "Animal id");
        return repo.getRequired(id, () -> new NotFoundException("Animal id=" + id + " not found"));
    }

    public Animal add(String name, String species, int age, int zooId) {
        validateAnimalFields(name, species, age, zooId);
        ensureZooExists(zooId);

        return repo.create(
                Animal.builder()
                        .name(name)
                        .species(species)
                        .age(age)
                        .zooId(zooId)
                        .build()
        );
    }

    public void update(int id, String name, String species, int age, int zooId) {
        requirePositive(id, "Animal id");
        validateAnimalFields(name, species, age, zooId);
        ensureZooExists(zooId);

        boolean ok = repo.update(
                Animal.builder()
                        .id(id)
                        .name(name)
                        .species(species)
                        .age(age)
                        .zooId(zooId)
                        .build()
        );

        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    public void updateAge(int id, int newAge) {
        requirePositive(id, "Animal id");
        if (newAge < 0) throw new ValidationException("Age cannot be negative");

        Animal current = getById(id);

        boolean ok = repo.update(
                Animal.builder()
                        .id(current.getId())
                        .name(current.getName())
                        .species(current.getSpecies())
                        .age(newAge)
                        .zooId(current.getZooId())
                        .build()
        );

        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    public void delete(int id) {
        requirePositive(id, "Animal id");
        boolean ok = repo.delete(id);
        if (!ok) throw new NotFoundException("Animal id=" + id + " not found");
    }

    private void ensureZooExists(int zooId) {
        // default method из CrudRepository
        zooRepo.getRequired(zooId, () -> new NotFoundException("Zoo id=" + zooId + " not found"));
    }

    private void validateAnimalFields(String name, String species, int age, int zooId) {
        if (name == null || name.trim().isEmpty())
            throw new ValidationException("Animal name cannot be empty");
        if (species == null || species.trim().isEmpty())
            throw new ValidationException("Species cannot be empty");
        if (age < 0)
            throw new ValidationException("Age cannot be negative");
        requirePositive(zooId, "zooId");
    }

    private void requirePositive(int value, String fieldName) {
        try {
            // static method из CrudRepository
            CrudRepository.requirePositiveInt(value, fieldName);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
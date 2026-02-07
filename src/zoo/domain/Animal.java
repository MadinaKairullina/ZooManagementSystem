package zoo.domain;

import java.util.Objects;

public class Animal {
    private int id;
    private String name;
    private String species;
    private int age;
    private int zooId;

    public Animal(int id, String name, String species, int age, int zooId) {
        this.id = id;
        setName(name);
        setSpecies(species);
        setAge(age);
        setZooId(zooId);
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
    public int getZooId() { return zooId; }

    // setters (Encapsulation + Validation)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Animal name cannot be empty");
        this.name = name.trim();
    }

    public void setSpecies(String species) {
        if (species == null || species.trim().isEmpty())
            throw new IllegalArgumentException("Species cannot be empty");
        this.species = species.trim();
    }

    public void setAge(int age) {
        if (age < 0)
            throw new IllegalArgumentException("Age cannot be negative");
        this.age = age;
    }

    public void setZooId(int zooId) {
        if (zooId <= 0)
            throw new IllegalArgumentException("zooId must be > 0");
        this.zooId = zooId;
    }

    @Override
    public String toString() {
        return "Animal{id=" + id + ", name='" + name + "', species='" + species +
                "', age=" + age + ", zooId=" + zooId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ===== BUILDER PATTERN =====
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int id = 0;
        private String name;
        private String species;
        private int age;
        private int zooId;

        private Builder() {}

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder species(String species) { this.species = species; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Builder zooId(int zooId) { this.zooId = zooId; return this; }

        public Animal build() {
            return new Animal(id, name, species, age, zooId);
        }
    }
}
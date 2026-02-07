package zoo.domain;

import java.util.Objects;

public class Zookeeper extends Person {
    private int id;
    private int experienceYears;
    private int zooId;

    public Zookeeper(int id, String name, int experienceYears, int zooId) {
        super(name);
        this.id = id;
        setExperienceYears(experienceYears);
        setZooId(zooId);
    }

    // Polymorphism
    @Override
    public String getRole() {
        return "Zookeeper";
    }

    // getters
    public int getId() { return id; }
    public int getExperienceYears() { return experienceYears; }
    public int getZooId() { return zooId; }

    // setters with validation
    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0)
            throw new IllegalArgumentException("Experience years cannot be negative");
        this.experienceYears = experienceYears;
    }

    public void setZooId(int zooId) {
        if (zooId <= 0)
            throw new IllegalArgumentException("zooId must be > 0");
        this.zooId = zooId;
    }

    @Override
    public String toString() {
        return "Zookeeper{id=" + id +
                ", name='" + getName() +
                "', experienceYears=" + experienceYears +
                ", zooId=" + zooId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zookeeper)) return false;
        Zookeeper that = (Zookeeper) o;
        return id == that.id;
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
        private int experienceYears;
        private int zooId;

        private Builder() {}

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder experienceYears(int years) { this.experienceYears = years; return this; }
        public Builder zooId(int zooId) { this.zooId = zooId; return this; }

        public Zookeeper build() {
            return new Zookeeper(id, name, experienceYears, zooId);
        }
    }
}
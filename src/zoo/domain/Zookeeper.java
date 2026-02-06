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
}
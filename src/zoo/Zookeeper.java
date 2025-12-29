package zoo;

import java.util.Objects;

public class Zookeeper extends Person {
    private int experienceYears;

    public Zookeeper(String name, int experienceYears) {
        super(name);
        this.experienceYears = experienceYears;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    @Override
    public String getRole() {
        return "Zookeeper";
    }

    @Override
    public String toString() {
        return "Zookeeper{name='" + name + "', experienceYears=" + experienceYears + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zookeeper)) return false;
        Zookeeper that = (Zookeeper) o;
        return experienceYears == that.experienceYears &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, experienceYears);
    }
}
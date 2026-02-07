package zoo.domain;

import java.util.Objects;

public class Zoo {
    private int id;
    private String name;
    private String city;

    public Zoo(int id, String name, String city) {
        this.id = id;
        setName(name);
        setCity(city);
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }

    // setters with validation (Encapsulation)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Zoo name cannot be empty");
        this.name = name.trim();
    }

    public void setCity(String city) {
        if (city == null || city.trim().isEmpty())
            throw new IllegalArgumentException("City cannot be empty");
        this.city = city.trim();
    }

    @Override
    public String toString() {
        return "Zoo{id=" + id + ", name='" + name + "', city='" + city + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zoo)) return false;
        Zoo zoo = (Zoo) o;
        return id == zoo.id;
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
        private String city;

        private Builder() {}

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder city(String city) { this.city = city; return this; }

        public Zoo build() {
            return new Zoo(id, name, city);
        }
    }
}
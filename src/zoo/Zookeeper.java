package zoo;

public class Zookeeper {
    private String name;
    private int experienceYears;

    public Zookeeper(String name, int experienceYears) {
        this.name = name;
        this.experienceYears = experienceYears;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void displayInfo() {
        System.out.println("Zookeeper: " + name + ", Experience: " + experienceYears + " years");
    }
}

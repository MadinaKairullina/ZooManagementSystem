package zoo;

public class Zoo {
    private String zooName;
    private String location;

    public Zoo(String zooName, String location) {
        this.zooName = zooName;
        this.location = location;
    }

    public String getZooName() {
        return zooName;
    }

    public void setZooName(String zooName) {
        this.zooName = zooName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void displayInfo() {
        System.out.println("Zoo: " + zooName + ", Location: " + location);
    }
}

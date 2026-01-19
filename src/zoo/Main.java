package zoo;

public class Main {
    public static void main(String[] args) {

        ZookeeperDAO dao = new ZookeeperDAO();

        try {
            dao.addZookeeper(new Zookeeper("Alex", 7));
            dao.addZookeeper(new Zookeeper("Maria", 12));

            System.out.println("All zookeepers:");
            for (Zookeeper z : dao.getAllZookeepers()) {
                System.out.println(z);
            }

            dao.updateExperience("Alex", 10);
            dao.deleteZookeeper("Maria");

            System.out.println("All zookeepers:");
            for (Zookeeper z : dao.getAllZookeepers()) {
                System.out.println(z);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
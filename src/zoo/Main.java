package zoo;

import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<Zookeeper> workers = new ArrayList<>();
        workers.add(new Zookeeper("Alex", 7));
        workers.add(new Zookeeper("Maria", 12));
        workers.add(new Zookeeper("Ivan", 5));
        System.out.println("All workers:");
        for (Zookeeper z : workers) {
            System.out.println(z);
        }
        System.out.println("\nWorkers with more than 6 years of experience:");
        for (Zookeeper z : workers) {
            if (z.getExperienceYears() > 6) {
                System.out.println(z);
            }
        }
        workers.sort((a, b) -> b.getExperienceYears() - a.getExperienceYears());
        System.out.println("\nSorted by experience:");
        for (Zookeeper z : workers) {
            System.out.println(z);
        }
    }
}
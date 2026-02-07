package zoo.ui;

import zoo.domain.Animal;
import zoo.domain.Zoo;
import zoo.domain.Zookeeper;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.service.AnimalService;
import zoo.service.ZooService;
import zoo.service.ZookeeperService;
import zoo.util.DataPool;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {

    private final ZooService zooService;
    private final AnimalService animalService;
    private final ZookeeperService zookeeperService;

    private final Scanner sc = new Scanner(System.in);

    public ConsoleMenu(ZooService zooService,
                       AnimalService animalService,
                       ZookeeperService zookeeperService) {
        this.zooService = zooService;
        this.animalService = animalService;
        this.zookeeperService = zookeeperService;
    }

    public void run() {
        while (true) {
            printMainMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> zooMenuLoop();
                case 2 -> animalMenuLoop();
                case 3 -> zookeeperMenuLoop();
                case 4 -> analyticsMenuLoop(); // ✅ DataPool
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Wrong option");
            }
        }
    }

    // ====== MAIN MENU ======
    private void printMainMenu() {
        System.out.println("\n=== ZOO MANAGEMENT ===");
        System.out.println("1. Zoos (CRUD)");
        System.out.println("2. Animals (CRUD)");
        System.out.println("3. Zookeepers (CRUD)");
        System.out.println("4. Search / Filter / Sort (DataPool)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    // ===================== ZOO MENU (CRUD) =====================
    private void zooMenuLoop() {
        while (true) {
            System.out.println("\n--- Zoos (CRUD) ---");
            System.out.println("1. Show all zoos");
            System.out.println("2. Show zoo by id");
            System.out.println("3. Add zoo");
            System.out.println("4. Update zoo");
            System.out.println("5. Delete zoo");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> zooService.getAll().forEach(System.out::println);
                    case 2 -> showZooById();
                    case 3 -> addZoo();
                    case 4 -> updateZoo();
                    case 5 -> deleteZoo();
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void showZooById() {
        System.out.print("Zoo ID: ");
        int id = readInt();
        Zoo z = zooService.getById(id);
        System.out.println(z);
    }

    private void addZoo() {
        System.out.print("Zoo name: ");
        String name = readLine();
        System.out.print("City: ");
        String city = readLine();

        zooService.add(name, city);
        System.out.println("Zoo added");
    }

    private void updateZoo() {
        System.out.print("Zoo ID to update: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New city: ");
        String city = readLine();

        zooService.update(id, name, city);
        System.out.println("Zoo updated");
    }

    private void deleteZoo() {
        System.out.print("Zoo ID to delete: ");
        int id = readInt();
        zooService.delete(id);
        System.out.println("Zoo deleted");
    }

    // ===================== ANIMAL MENU (CRUD) =====================
    private void animalMenuLoop() {
        while (true) {
            System.out.println("\n--- Animals (CRUD) ---");
            System.out.println("1. Show all animals");
            System.out.println("2. Show animal by id");
            System.out.println("3. Show animals by zoo");
            System.out.println("4. Add animal");
            System.out.println("5. Update animal (all fields)");
            System.out.println("6. Update animal age only");
            System.out.println("7. Delete animal");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> animalService.getAll().forEach(System.out::println);
                    case 2 -> showAnimalById();
                    case 3 -> showAnimalsByZoo();
                    case 4 -> addAnimal();
                    case 5 -> updateAnimal();
                    case 6 -> updateAnimalAge();
                    case 7 -> deleteAnimal();
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void showAnimalById() {
        System.out.print("Animal ID: ");
        int id = readInt();
        Animal a = animalService.getById(id);
        System.out.println(a);
    }

    private void showAnimalsByZoo() {
        System.out.print("Zoo ID: ");
        int zooId = readInt();
        List<Animal> animals = animalService.getByZooId(zooId);
        if (animals.isEmpty()) System.out.println("(no animals)");
        else animals.forEach(System.out::println);
    }

    private void addAnimal() {
        System.out.print("Name: ");
        String name = readLine();
        System.out.print("Species: ");
        String species = readLine();
        System.out.print("Age: ");
        int age = readInt();
        System.out.print("Zoo ID: ");
        int zooId = readInt();

        animalService.add(name, species, age, zooId);
        System.out.println("Animal added");
    }

    private void updateAnimal() {
        System.out.print("Animal ID to update: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New species: ");
        String species = readLine();
        System.out.print("New age: ");
        int age = readInt();
        System.out.print("New zoo ID: ");
        int zooId = readInt();

        animalService.update(id, name, species, age, zooId);
        System.out.println("Animal updated");
    }

    private void updateAnimalAge() {
        System.out.print("Animal ID: ");
        int id = readInt();
        System.out.print("New age: ");
        int newAge = readInt();

        animalService.updateAge(id, newAge);
        System.out.println("Animal age updated");
    }

    private void deleteAnimal() {
        System.out.print("Animal ID to delete: ");
        int id = readInt();
        animalService.delete(id);
        System.out.println("Animal deleted");
    }

    // ===================== ZOOKEEPER MENU (CRUD) =====================
    private void zookeeperMenuLoop() {
        while (true) {
            System.out.println("\n--- Zookeepers (CRUD) ---");
            System.out.println("1. Show all zookeepers");
            System.out.println("2. Show zookeeper by id");
            System.out.println("3. Show zookeepers by zoo");
            System.out.println("4. Add zookeeper");
            System.out.println("5. Update zookeeper (all fields)");
            System.out.println("6. Update experience only");
            System.out.println("7. Delete zookeeper");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> zookeeperService.getAll().forEach(System.out::println);
                    case 2 -> showZookeeperById();
                    case 3 -> showZookeepersByZoo();
                    case 4 -> addZookeeper();
                    case 5 -> updateZookeeper();
                    case 6 -> updateZookeeperExperience();
                    case 7 -> deleteZookeeper();
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void showZookeeperById() {
        System.out.print("Zookeeper ID: ");
        int id = readInt();
        Zookeeper z = zookeeperService.getById(id);
        System.out.println(z);
    }

    private void showZookeepersByZoo() {
        System.out.print("Zoo ID: ");
        int zooId = readInt();
        List<Zookeeper> list = zookeeperService.getByZooId(zooId);
        if (list.isEmpty()) System.out.println("(no zookeepers)");
        else list.forEach(System.out::println);
    }

    private void addZookeeper() {
        System.out.print("Name: ");
        String name = readLine();
        System.out.print("Experience years: ");
        int exp = readInt();
        System.out.print("Zoo ID: ");
        int zooId = readInt();

        zookeeperService.add(name, exp, zooId);
        System.out.println("Zookeeper added");
    }

    private void updateZookeeper() {
        System.out.print("Zookeeper ID to update: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New experience years: ");
        int exp = readInt();
        System.out.print("New zoo ID: ");
        int zooId = readInt();

        zookeeperService.update(id, name, exp, zooId);
        System.out.println("Zookeeper updated");
    }

    private void updateZookeeperExperience() {
        System.out.print("Zookeeper ID: ");
        int id = readInt();
        System.out.print("New experience years: ");
        int exp = readInt();

        zookeeperService.updateExperience(id, exp);
        System.out.println("Zookeeper experience updated");
    }

    private void deleteZookeeper() {
        System.out.print("Zookeeper ID to delete: ");
        int id = readInt();
        zookeeperService.delete(id);
        System.out.println("Zookeeper deleted");
    }

    // ===================== ANALYTICS MENU (DataPool) =====================
    private void analyticsMenuLoop() {
        while (true) {
            System.out.println("\n--- Search / Filter / Sort (DataPool) ---");
            System.out.println("1. Animals: search by name (contains)");
            System.out.println("2. Animals: filter by species");
            System.out.println("3. Animals: sort by age (asc)");
            System.out.println("4. Zookeepers: sort by experience (desc)");
            System.out.println("5. Find oldest animal in a zoo");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();

            try {
                switch (choice) {
                    case 1 -> searchAnimalByName();
                    case 2 -> filterAnimalsBySpecies();
                    case 3 -> sortAnimalsByAge();
                    case 4 -> sortZookeepersByExperience();
                    case 5 -> oldestAnimalInZoo();
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void searchAnimalByName() {
        System.out.print("Enter part of name: ");
        String part = readLine().toLowerCase();

        DataPool<Animal> pool = new DataPool<>();
        pool.addAll(animalService.getAll());

        List<Animal> res = pool.filter(a -> a.getName().toLowerCase().contains(part));
        if (res.isEmpty()) System.out.println("(no matches)");
        else res.forEach(System.out::println);
    }

    private void filterAnimalsBySpecies() {
        System.out.print("Species: ");
        String species = readLine().toLowerCase();

        DataPool<Animal> pool = new DataPool<>();
        pool.addAll(animalService.getAll());

        List<Animal> res = pool.filter(a -> a.getSpecies().toLowerCase().equals(species));
        if (res.isEmpty()) System.out.println("(no matches)");
        else res.forEach(System.out::println);
    }

    private void sortAnimalsByAge() {
        DataPool<Animal> pool = new DataPool<>();
        pool.addAll(animalService.getAll());

        List<Animal> sorted = pool.sort(Comparator.comparingInt(Animal::getAge));
        if (sorted.isEmpty()) System.out.println("(no animals)");
        else sorted.forEach(System.out::println);
    }

    private void sortZookeepersByExperience() {
        DataPool<Zookeeper> pool = new DataPool<>();
        pool.addAll(zookeeperService.getAll());

        List<Zookeeper> sorted = pool.sort(Comparator.comparingInt(Zookeeper::getExperienceYears).reversed());
        if (sorted.isEmpty()) System.out.println("(no zookeepers)");
        else sorted.forEach(System.out::println);
    }

    private void oldestAnimalInZoo() {
        System.out.print("Zoo ID: ");
        int zooId = readInt();

        DataPool<Animal> pool = new DataPool<>();
        pool.addAll(animalService.getByZooId(zooId));

        Optional<Animal> oldest = pool.sort(Comparator.comparingInt(Animal::getAge).reversed())
                .stream()
                .findFirst();

        if (oldest.isPresent()) {
            System.out.println("Oldest: " + oldest.get());
        } else {
            System.out.println("(no animals)");
        }
    }

    // ====== HELPERS ======
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private String readLine() {
        String s;
        do {
            s = sc.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }
}
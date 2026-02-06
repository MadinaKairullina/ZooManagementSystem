package zoo;

import zoo.domain.Animal;
import zoo.domain.Zoo;
import zoo.domain.Zookeeper;
import zoo.exception.NotFoundException;
import zoo.exception.ValidationException;
import zoo.repository.jdbc.JdbcAnimalRepository;
import zoo.repository.jdbc.JdbcZooRepository;
import zoo.repository.jdbc.JdbcZookeeperRepository;
import zoo.service.AnimalService;
import zoo.service.ZooService;
import zoo.service.ZookeeperService;
import zoo.util.DataPool;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Services (UI -> Service -> Repository(JDBC))
    private static ZooService zooService;
    private static AnimalService animalService;
    private static ZookeeperService zookeeperService;

    public static void main(String[] args) {
        // init services once
        zooService = new ZooService(new JdbcZooRepository());
        animalService = new AnimalService(new JdbcAnimalRepository());
        zookeeperService = new ZookeeperService(new JdbcZookeeperRepository());

        runMainMenu();
    }

    // ===================== MAIN MENU =====================
    private static void runMainMenu() {
        while (true) {
            try {
                System.out.println("\n=== ZOO MANAGEMENT ===");
                System.out.println("1) List all zoos");
                System.out.println("2) Add zoo");
                System.out.println("3) Update zoo");
                System.out.println("4) Delete zoo");
                System.out.println("5) Manage one zoo (Animals & Zookeepers)");
                System.out.println("0) Exit");
                System.out.print("Choose: ");

                int choice = readInt();
                switch (choice) {
                    case 1 -> listZoos();
                    case 2 -> addZoo();
                    case 3 -> updateZoo();
                    case 4 -> deleteZoo();
                    case 5 -> manageZoo();
                    case 0 -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Wrong option!");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (RuntimeException e) {
                // catches DB runtime errors thrown from repositories
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    // ===================== ZOO CRUD =====================
    private static void listZoos() {
        List<Zoo> zoos = zooService.getAll();
        if (zoos.isEmpty()) {
            System.out.println("No zoos found.");
            return;
        }
        System.out.println("\n--- Zoos ---");
        zoos.forEach(System.out::println);
    }

    private static void addZoo() {
        System.out.print("Zoo name: ");
        String name = readLine();
        System.out.print("City: ");
        String city = readLine();

        zooService.add(new Zoo(0, name, city));
        System.out.println("Zoo added.");
    }

    private static void updateZoo() {
        System.out.print("Zoo id: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New city: ");
        String city = readLine();

        zooService.update(new Zoo(id, name, city));
        System.out.println("Zoo updated.");
    }

    private static void deleteZoo() {
        System.out.print("Zoo id to delete: ");
        int id = readInt();
        zooService.delete(id);
        System.out.println("Zoo deleted.");
    }

    // ===================== MANAGE ONE ZOO =====================
    private static void manageZoo() {
        System.out.print("Enter zoo id: ");
        int zooId = readInt();

        // will throw NotFoundException if zoo doesn't exist
        Zoo zoo = zooService.getById(zooId);
        System.out.println("Managing: " + zoo);

        while (true) {
            try {
                System.out.println("\n=== Manage Zoo ID: " + zooId + " ===");
                System.out.println("1) List animals");
                System.out.println("2) Add animal");
                System.out.println("3) Update animal");
                System.out.println("4) Delete animal");

                System.out.println("5) List zookeepers");
                System.out.println("6) Add zookeeper");
                System.out.println("7) Update zookeeper");
                System.out.println("8) Delete zookeeper");

                System.out.println("9) DataPool: Animals (Search/Filter/Sort)");
                System.out.println("10) DataPool: Zookeepers (Search/Filter/Sort)");
                System.out.println("0) Back");
                System.out.print("Choose: ");

                int choice = readInt();
                switch (choice) {
                    case 1 -> listAnimals(zooId);
                    case 2 -> addAnimal(zooId);
                    case 3 -> updateAnimal(zooId);
                    case 4 -> deleteAnimal();

                    case 5 -> listZookeepers(zooId);
                    case 6 -> addZookeeper(zooId);
                    case 7 -> updateZookeeper(zooId);
                    case 8 -> deleteZookeeper();

                    case 9 -> animalPoolMenu(zooId);
                    case 10 -> zookeeperPoolMenu(zooId);

                    case 0 -> { return; }
                    default -> System.out.println("Wrong option!");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    // ===================== ANIMALS CRUD =====================
    private static void listAnimals(int zooId) {
        List<Animal> animals = animalService.getByZooId(zooId);
        if (animals.isEmpty()) {
            System.out.println("No animals found for zoo_id=" + zooId);
            return;
        }
        System.out.println("\n--- Animals ---");
        animals.forEach(System.out::println);
    }

    private static void addAnimal(int zooId) {
        System.out.print("Animal name: ");
        String name = readLine();
        System.out.print("Species: ");
        String species = readLine();
        System.out.print("Age: ");
        int age = readInt();

        Animal created = animalService.add(name, species, age, zooId);
        System.out.println("Added: " + created);
    }

    private static void updateAnimal(int zooId) {
        System.out.print("Animal id: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New species: ");
        String species = readLine();
        System.out.print("New age: ");
        int age = readInt();

        animalService.update(id, name, species, age, zooId);
        System.out.println("Animal updated.");
    }

    private static void deleteAnimal() {
        System.out.print("Animal id to delete: ");
        int id = readInt();
        animalService.delete(id);
        System.out.println("Animal deleted.");
    }

    // ===================== ZOOKEEPER CRUD =====================
    private static void listZookeepers(int zooId) {
        List<Zookeeper> keepers = zookeeperService.getByZooId(zooId);
        if (keepers.isEmpty()) {
            System.out.println("No zookeepers found for zoo_id=" + zooId);
            return;
        }
        System.out.println("\n--- Zookeepers ---");
        keepers.forEach(System.out::println);
    }

    private static void addZookeeper(int zooId) {
        System.out.print("Zookeeper name: ");
        String name = readLine();
        System.out.print("Experience years: ");
        int exp = readInt();

        Zookeeper created = zookeeperService.add(name, exp, zooId);
        System.out.println("Added: " + created);
    }

    private static void updateZookeeper(int zooId) {
        System.out.print("Zookeeper id: ");
        int id = readInt();
        System.out.print("New name: ");
        String name = readLine();
        System.out.print("New experience years: ");
        int exp = readInt();

        zookeeperService.update(id, name, exp, zooId);
        System.out.println("Zookeeper updated.");
    }

    private static void deleteZookeeper() {
        System.out.print("Zookeeper id to delete: ");
        int id = readInt();
        zookeeperService.delete(id);
        System.out.println("Zookeeper deleted.");
    }

    // ===================== DATA POOL: ANIMALS =====================
    private static void animalPoolMenu(int zooId) {
        List<Animal> animals = animalService.getByZooId(zooId);

        DataPool<Animal> pool = new DataPool<>();
        pool.addAll(animals);

        while (true) {
            System.out.println("\n=== DataPool: Animals (zoo_id=" + zooId + ") ===");
            System.out.println("1) Search by name");
            System.out.println("2) Filter by min age");
            System.out.println("3) Filter by species");
            System.out.println("4) Filter by age range");
            System.out.println("5) Sort (field + order)");
            System.out.println("6) Show all");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter name: ");
                        String name = readLine();
                        Animal found = pool.findFirst(a -> a.getName().equalsIgnoreCase(name))
                                .orElseThrow(() -> new NotFoundException("Animal with name '" + name + "' not found"));
                        System.out.println("Found: " + found);
                    }
                    case 2 -> {
                        System.out.print("Min age: ");
                        int minAge = readInt();
                        if (minAge < 0) throw new ValidationException("Age cannot be negative");
                        List<Animal> result = pool.filter(a -> a.getAge() >= minAge);
                        if (result.isEmpty()) throw new NotFoundException("No animals found with age >= " + minAge);
                        printAnimals(result);
                    }
                    case 3 -> {
                        System.out.print("Species: ");
                        String species = readLine();
                        List<Animal> result = pool.filter(a -> a.getSpecies().equalsIgnoreCase(species));
                        if (result.isEmpty()) throw new NotFoundException("No animals found with species '" + species + "'");
                        printAnimals(result);
                    }
                    case 4 -> {
                        System.out.print("Age from: ");
                        int from = readInt();
                        System.out.print("Age to: ");
                        int to = readInt();
                        if (from < 0 || to < 0) throw new ValidationException("Age cannot be negative");
                        if (from > to) throw new ValidationException("Age 'from' cannot be greater than 'to'");
                        List<Animal> result = pool.filter(a -> a.getAge() >= from && a.getAge() <= to);
                        if (result.isEmpty()) throw new NotFoundException("No animals found in age range " + from + ".." + to);
                        printAnimals(result);
                    }
                    case 5 -> {
                        Comparator<Animal> comp = chooseAnimalComparator();
                        List<Animal> sorted = pool.sort(comp);
                        if (sorted.isEmpty()) throw new NotFoundException("No animals to sort");
                        printAnimals(sorted);
                    }
                    case 6 -> {
                        List<Animal> all = pool.getAll();
                        if (all.isEmpty()) throw new NotFoundException("No animals in this zoo");
                        printAnimals(all);
                    }
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option!");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static Comparator<Animal> chooseAnimalComparator() {
        System.out.println("Sort by:");
        System.out.println("1) Age");
        System.out.println("2) Name");
        System.out.println("3) Species");
        System.out.print("Choose: ");
        int field = readInt();

        System.out.print("Order (1=ASC, 2=DESC): ");
        int order = readInt();

        Comparator<Animal> comp = switch (field) {
            case 1 -> Comparator.comparingInt(Animal::getAge);
            case 2 -> Comparator.comparing(Animal::getName, String.CASE_INSENSITIVE_ORDER);
            case 3 -> Comparator.comparing(Animal::getSpecies, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparingInt(Animal::getId);
        };

        if (order == 2) comp = comp.reversed();
        return comp;
    }

    private static void printAnimals(List<Animal> list) {
        System.out.println("\n--- Results (" + list.size() + ") ---");
        list.forEach(System.out::println);
    }

    // ===================== DATA POOL: ZOOKEEPERS =====================
    private static void zookeeperPoolMenu(int zooId) {
        List<Zookeeper> keepers = zookeeperService.getByZooId(zooId);

        DataPool<Zookeeper> pool = new DataPool<>();
        pool.addAll(keepers);

        while (true) {
            System.out.println("\n=== DataPool: Zookeepers (zoo_id=" + zooId + ") ===");
            System.out.println("1) Search by name");
            System.out.println("2) Filter by min experience");
            System.out.println("3) Filter by experience range");
            System.out.println("4) Sort (field + order)");
            System.out.println("5) Show all");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter name: ");
                        String name = readLine();
                        Zookeeper found = pool.findFirst(z -> z.getName().equalsIgnoreCase(name))
                                .orElseThrow(() -> new NotFoundException("Zookeeper with name '" + name + "' not found"));
                        System.out.println("Found: " + found);
                    }
                    case 2 -> {
                        System.out.print("Min experience years: ");
                        int min = readInt();
                        if (min < 0) throw new ValidationException("Experience years cannot be negative");
                        List<Zookeeper> result = pool.filter(z -> z.getExperienceYears() >= min);
                        if (result.isEmpty()) throw new NotFoundException("No zookeepers found with experience >= " + min);
                        printZookeepers(result);
                    }
                    case 3 -> {
                        System.out.print("Experience from: ");
                        int from = readInt();
                        System.out.print("Experience to: ");
                        int to = readInt();
                        if (from < 0 || to < 0) throw new ValidationException("Experience years cannot be negative");
                        if (from > to) throw new ValidationException("'from' cannot be greater than 'to'");
                        List<Zookeeper> result = pool.filter(z -> z.getExperienceYears() >= from && z.getExperienceYears() <= to);
                        if (result.isEmpty()) throw new NotFoundException("No zookeepers found in range " + from + ".." + to);
                        printZookeepers(result);
                    }
                    case 4 -> {
                        Comparator<Zookeeper> comp = chooseZookeeperComparator();
                        List<Zookeeper> sorted = pool.sort(comp);
                        if (sorted.isEmpty()) throw new NotFoundException("No zookeepers to sort");
                        printZookeepers(sorted);
                    }
                    case 5 -> {
                        List<Zookeeper> all = pool.getAll();
                        if (all.isEmpty()) throw new NotFoundException("No zookeepers in this zoo");
                        printZookeepers(all);
                    }
                    case 0 -> { return; }
                    default -> System.out.println("Wrong option!");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static Comparator<Zookeeper> chooseZookeeperComparator() {
        System.out.println("Sort by:");
        System.out.println("1) Experience years");
        System.out.println("2) Name");
        System.out.print("Choose: ");
        int field = readInt();

        System.out.print("Order (1=ASC, 2=DESC): ");
        int order = readInt();

        Comparator<Zookeeper> comp = switch (field) {
            case 1 -> Comparator.comparingInt(Zookeeper::getExperienceYears);
            case 2 -> Comparator.comparing(Zookeeper::getName, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparingInt(Zookeeper::getId);
        };

        if (order == 2) comp = comp.reversed();
        return comp;
    }

    private static void printZookeepers(List<Zookeeper> list) {
        System.out.println("\n--- Results (" + list.size() + ") ---");
        list.forEach(System.out::println);
    }

    // ===================== INPUT HELPERS =====================
    private static int readInt() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private static String readLine() {
        String s = sc.nextLine();
        while (s != null && s.trim().isEmpty()) {
            s = sc.nextLine();
        }
        return s == null ? "" : s.trim();
    }
}
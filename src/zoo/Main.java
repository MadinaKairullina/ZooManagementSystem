package zoo;

public class Main {
    public static void main(String[] args) {

        Animals animal1 = new Animals("Leo", "Lion", 5);
        Animals animal2 = new Animals("Molly", "Elephant", 10);

        Zookeeper keeper = new Zookeeper("Alex", 7);

        Zoo zoo = new Zoo("Astana Zoo", "Kazakhstan");

        animal1.displayInfo();
        animal2.displayInfo();
        keeper.displayInfo();
        zoo.displayInfo();

        if (animal1.getAge() > animal2.getAge()) {
            System.out.println(animal1.getName() + " is older than " + animal2.getName());
        } else {
            System.out.println(animal2.getName() + " is older than " + animal1.getName());
        }
    }
}
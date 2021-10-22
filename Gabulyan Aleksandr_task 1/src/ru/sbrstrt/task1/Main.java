package ru.sbrstrt.task1;

import ru.sbrstrt.task1.model.Person;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static String addName(String typeOfName) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type the " + typeOfName + " name");
        String name = scanner.next();

        while (!name.matches("[a-zA-Z]+")) {
            System.out.println(typeOfName + " name should contain only letters");
            name = scanner.next();
        }
        return name;
    }

    public static int checkNumber() {
        Scanner scanner = new Scanner(System.in);

        String string = scanner.next();

        while (!string.matches("\\d+")) {
            System.out.println("Type the number");
            string = scanner.next();
        }
        return Integer.parseInt(string);
    }

    private static void printMenu() {
        System.out.println("\nType the number to:\n" +
                new Menu().items.get(0).name + "\n" +
                new Menu().items.get(1).name + "\n" +
                new Menu().items.get(2).name + "\n" +
                new Menu().items.get(3).name + "\n" +
                new Menu().items.get(4).name + "\n" +
                new Menu().items.get(5).name + "\n" +
                new Menu().items.get(6).name + "\n" +
                new Menu().items.get(7).name + "\n");
    }

    private interface Exec {
        void exec(List<Person> data);
    }

    private static class MenuItem {
        private final String name;
        private final Exec exec;

        public MenuItem(String name, Exec exec) {
            this.name = name;
            this.exec = exec;
        }

    }

    private static class Menu {
        private final List<MenuItem> items = new ArrayList<>();

        private Menu() {
            items.add(new MenuItem("1) add a person", personList -> {
                Person person1 = new Person();

                person1.setFirstName(addName("First"));
                person1.setLastName(addName("Last"));

                personList.add(person1);
            }));

            items.add(new MenuItem("2) add a few people", personList -> {
                System.out.println("Type the number of people you want to add");
                int number = checkNumber();

                int i = 1;
                while (i <= number) {
                    Person personAll = new Person();
                    System.out.println("Person number " + i);
                    personAll.setFirstName(addName("First"));
                    personAll.setLastName(addName("Last"));

                    personList.add(personAll);
                    i++;
                }
            }));

            items.add(new MenuItem("3) show the list of people", personList -> personList.forEach(System.out::println)));

            items.add(new MenuItem("4) show sorted unique", personList -> personList.stream()
                    .sorted(Comparator.comparing(Person::getLastName))
                    .collect(Collectors.toMap(Person::getLastName, (p) -> p, (p, q) -> p))
                    .values()
                    .forEach(System.out::println)));

            items.add(new MenuItem("5) save to file", personList -> {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Type the name of file you'd like to add your list of people");
                String fileName = scanner.next();
                File file = new File("src/source/" + fileName + ".txt");

                try (FileWriter fileWriter = new FileWriter(file)) {
                    if (file.exists()) {
                        throw new FileAlreadyExistsException("Check this folder: src/source/" + fileName + ".txt");
                    } else
                        file.createNewFile();

                    for (Person p : personList) {
                        fileWriter.write(p.getFirstName() + " " + p.getLastName() + ";\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            items.add(new MenuItem("6) read from file", personList -> {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Type the name of your .txt file");
                String fileName = scanner.next();

                try (FileReader fileReader = new FileReader("src/source/" + fileName + ".txt")) {
                    Scanner sc = new Scanner(fileReader);
                    while (sc.hasNextLine()) {
                        String stringOfPerson = sc.nextLine();
                        String fullPerson = stringOfPerson.substring(0, stringOfPerson.length() - 1);
                        String[] person = fullPerson.split(" ");
                        personList.add(new Person(person[0], person[1]));
                    }
                } catch (IOException e) {
                    System.out.println("Exception " + e.getMessage());
                    printMenu();
                }
            }));

            items.add(new MenuItem("7) clear data from last added file", personList -> {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Type the name of your .txt file");
                String fileName = scanner.next();

                try (FileReader fileReader = new FileReader("src/source/" + fileName + ".txt")) {
                    Scanner sc = new Scanner(fileReader);
                    while (sc.hasNextLine()) {
                        String stringOfPerson = sc.nextLine();
                        String fullPerson = stringOfPerson.substring(0, stringOfPerson.length() - 1);
                        String[] person = fullPerson.split(" ");
                        personList.removeIf((p) -> p.getFirstName().equals(person[0]) & p.getLastName().equals(person[1]));
                    }
                } catch (IOException e) {
                    System.out.println("Exception " + e.getMessage());
                    printMenu();
                }
            }));

            items.add(new MenuItem("8) exit", personList -> {
                Scanner scanner = new Scanner(System.in);

                scanner.close();
                System.out.println("You are out");
            }));
        }
    }


    // TODO: 21.10.2021 почему-то после ввода первой цифры и получения соответствующего результата, не выводится сразу меню, а нужно ввести какое-либо значение для этого
    // TODO: 21.10.2021 во все следующие разы этого делать не требуется
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> people = new ArrayList<>();
        Menu menu = new Menu();
        Exec exec;

        do {
            printMenu();
            int action = checkNumber();

            switch (action) {
                case 1:
                    exec = menu.items.get(0).exec;
                    exec.exec(people);
                    break;
                case 2:
                    exec = menu.items.get(1).exec;
                    exec.exec(people);
                    break;
                case 3:
                    exec = menu.items.get(2).exec;
                    exec.exec(people);
                    break;
                case 4:
                    exec = menu.items.get(3).exec;
                    exec.exec(people);
                    break;
                case 5:
                    exec = menu.items.get(4).exec;
                    exec.exec(people);
                    break;
                case 6:
                    exec = menu.items.get(5).exec;
                    exec.exec(people);
                    break;
                case 7:
                    exec = menu.items.get(6).exec;
                    exec.exec(people);
                    break;
                case 8:
                    exec = menu.items.get(7).exec;
                    exec.exec(people);
                    return;
                default:
                    System.out.println("You should type the number from 1 to 8");
                    printMenu();
                    break;
            }
        } while (scanner.hasNext());
    }
}
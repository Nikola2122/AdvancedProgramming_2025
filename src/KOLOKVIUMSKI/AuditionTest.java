package KOLOKVIUMSKI;

import java.util.*;

class Candidate implements Comparable<Candidate>{
    String name;
    String code;
    int age;

    public Candidate(String name, String code, int age) {
        this.name = name;
        this.age = age;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Candidate o) {
        return Comparator.comparing(Candidate::getCode).compare(this, o);
    }
}

class Audition{
    Map<String, Set<Candidate>> map;

    public Audition() {
        this.map = new TreeMap<>();
    }

    void addParticpant(String city, String code, String name, int age){
        map.putIfAbsent(city, new TreeSet<>());
        Candidate c = new Candidate(name, code, age);
        map.get(city).add(c);
    }

    void listByCity(String city){
        map.get(city).stream().
                sorted(Comparator.comparing(Candidate::getName).thenComparing(Candidate::getAge)).
                forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
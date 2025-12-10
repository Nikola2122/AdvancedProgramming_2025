package AUD4;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Person implements Comparable<Person> {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.age, o.age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
public class OldestPerson {
    public static List<Person> readData(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines().map(i->{
            String [] arr = i.split(" ");
            return new Person(arr[0],Integer.parseInt(arr[1]));
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\oldestPerson.txt");
        List<Person> persons = readData(new FileInputStream(file));
        Optional<Person> person = persons.stream().max(Person::compareTo);
        if (person.isPresent()) {
            System.out.println(person.get());
        }
    }
}

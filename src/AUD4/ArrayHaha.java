package AUD4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    int i;
    String name;

    public Student(int i, String name) {
        this.i = i;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return i == student.i;
    }

    @Override
    public String toString() {
        return "Student{" +
                "i=" + i +
                ", name='" + name + '\'' +
                '}';
    }
}

public class ArrayHaha {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<Student>();
        Student s1 = new Student(1, "John");
        students.add(s1);
        Student s2 = new Student(2, "Jane");
        students.add(s2);
        Student s3 = new Student(1, "Mary");

        Student s4 = students.get(students.indexOf(s3));

        Predicate<Student> f = s -> s.name.equals("Mary");

        students.removeIf(f);


        System.out.println(students);

        System.out.println(s4);
        Supplier<Student> f1 = () -> new Student(1, "John");

        ArrayList<Student> students1 = IntStream.range(0,1).mapToObj(i->new Student(i,"d")).
                collect(Collectors.toCollection(ArrayList::new));


    }
}

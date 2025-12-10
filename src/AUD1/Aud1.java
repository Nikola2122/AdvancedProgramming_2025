package AUD1;

class Person{
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Age: %d", name, age);
    }
}

public class Aud1 {
    public static void main(String[] args) {
        Person p1 = new Person();
        Person p2 = new Person("Nikola",21);

        System.out.println(p2);
    }
}

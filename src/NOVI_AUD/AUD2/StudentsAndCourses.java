package NOVI_AUD.AUD2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Student{
    private String name;
    private int index;
    private int grade;
    private double attendance;

    public Student(String name, int index, int grade, double attendance) {
        this.name = name;
        this.index = index;
        this.grade = grade;
        this.attendance = attendance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        if(grade<5 || grade>10){
            throw new IllegalArgumentException("grade");
        }
        this.grade = grade;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(float attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return String.format("%s, %d, %d, %.2f", name, index, grade, attendance);
    }
}
class Course{
    private String name;
    private int maxStudents;
    private List<Student> students;

    public Course(String name, int maxStudents) {
        this.name = name;
        this.maxStudents = maxStudents;
        this.students = new ArrayList<>();
    }

    void enroll(Supplier<Student> supplier){
        if(students.size()<maxStudents){
            students.add(supplier.get());
        }
    }

    void forEach(Consumer<Student> action){
        students.forEach(action);
    }

    int count(Predicate<Student> condition){
        return (int) this.students.stream().filter(condition).count();
    }

    Optional<Student> findFirst(Predicate<Student> condition){
        return this.students.stream().filter(condition).findFirst();
    }

    List<Student> filter(Predicate<Student> condition){
        return this.students.stream().filter(condition).toList();
    }

    List<String> mapToLabels(Function<Student, String> mapper){
        return this.students.stream().map(mapper).toList();
    }

    void mutate(Consumer<Student> mutator){
        this.students.forEach(mutator);
    }

    void conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator){
        this.students.stream().filter(condition).forEach(mutator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(maxStudents).append("\n");
        sb.append(this.students.stream().map(Student::toString).collect(Collectors.joining("\n")));

        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}

public class StudentsAndCourses {

    static void inputAndOutput(Course course, InputStream inputStream, OutputStream outputStream){
        Scanner sc = new Scanner(inputStream);
        String line;
        while(!(line=sc.nextLine()).equals("end")){
            String [] arr = line.split(",");
            course.enroll(()->
                 new Student(arr[0],Integer.parseInt(arr[1]),Integer.parseInt(arr[2]),Double.parseDouble(arr[3]))
            );
        }
        PrintWriter writer = new PrintWriter(outputStream);
        course.forEach(writer::println);

        Predicate<Student> p1 = (s) -> s.getGrade()>=6;
        Predicate<Student> p2 = (s) -> s.getAttendance()>=70;
        course.filter(p1.and(p2));

        Optional<Student> student = course.findFirst(s->s.getGrade()>9);
        writer.println(student.get());

        course.mutate(s->s.setGrade(5));

        course.conditionalMutate(s->s.getAttendance()>=90, s->s.setGrade(10));

        List<String> str = course.mapToLabels(Student::toString);
        writer.println(str);
        writer.println(course);
        writer.flush();
    }

    public static void main(String[] args) {
        Course course = new Course("NP", 3);
        inputAndOutput(course, System.in, System.out);
    }
}

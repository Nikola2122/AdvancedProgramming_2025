package AUD4;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StudentHere implements Comparable<StudentHere> {
    private String name;
    private String surname;
    private List<Double> points;
    private char grade;
    private static final List<Double> pointsPercent = List.of(0.25, 0.3, 0.45);

    public StudentHere(String name, String surname, List<Double> points) {
        this.name = name;
        this.surname = surname;
        this.points = points;
        setGrade();
    }

    public static StudentHere createStudent(String line){
        String [] arr = line.split(":");
        String name = arr[0];
        String surname = arr[1];
        List<Double> points = IntStream.range(2,arr.length).mapToDouble(i->Double.parseDouble(arr[i])).boxed().toList();
        return new StudentHere(name, surname, points);
    }
    double totalPoints(){
        return IntStream.range(0, points.size()).mapToDouble(i-> points.get(i) * pointsPercent.get(i)).sum();
    }

    public void setGrade() {
        double points = totalPoints();
        if(points >= 90){
            grade = 'A';
        }
        else if(points >= 80){
            grade = 'B';
        }
        else if(points >= 70){
            grade = 'C';
        }
        else if(points >= 60){
            grade = 'D';
        }
        else if(points >= 50){
            grade = 'E';
        }
        else{
            grade = 'F';
        }
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + grade;
    }

    public String forFile(){
        String pointsString = points.stream().map(s->Double.toString(s)).collect(Collectors.joining(" "));
        return String.format("%s %s %s %.2f %c", surname,
                name, pointsString, totalPoints(), grade);
    }

    public char getGrade() {
        return grade;
    }

    @Override
    public int compareTo(StudentHere o) {
        return Character.compare(grade, o.grade);
    }
}


class Course {
    private List<StudentHere>  students;
    private Map<Character, Integer> distribution;

    public Course() {
        students = new ArrayList<>();
    }
    public void makeDistribution(){
        distribution = new HashMap<>();
        students.forEach(s->{
            if(distribution.get(s.getGrade())==null){
                distribution.put(s.getGrade(),1);
            }
            else{
                distribution.put(s.getGrade(),distribution.get(s.getGrade())+1);
            }
        });
    }

    public void ReadData(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        this.students = reader.lines().map(StudentHere::createStudent).toList();
        makeDistribution();
    }

    public void OutputToFile(OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);
        this.students.stream().sorted().forEach(s-> writer.println(s.forFile()));
        writer.flush();
        writer.close();
    }

    public void OutputToTerminalSorted(OutputStream stream){
        PrintWriter writer = new PrintWriter(stream);
        this.students.stream().sorted().forEach(writer::println);
        writer.flush();
    }

    public void PrintDistribution(OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);
        distribution.keySet().stream().sorted().forEach(s->{
            writer.println(String.format("%c -> %d", s, distribution.get(s)));
        });
        writer.flush();
        writer.close();
    }
}

public class CourseGrades{
    public static void main(String[] args) throws FileNotFoundException {
        File fileInput = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\CourseGradesInput.txt");
        File fileOutput = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\CourseGradesOutput.txt");

        Course course = new Course();
        course.ReadData(new FileInputStream(fileInput));
        course.OutputToTerminalSorted(System.out);
        course.PrintDistribution(System.out);
        course.OutputToFile(new FileOutputStream(fileOutput));
    }
}
package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class Student2{
    String index;
    List<Integer> points;

    public Student2(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    double summaryPoints(){
        return points.stream().mapToDouble(i -> i).sum()/10;
    }

    boolean isFailed(){
        return this.points.size() < 8;
    }

    int getYear(){
        return 20 - Integer.parseInt(this.index.substring(0,2));
    }

    @Override
    public String toString() {
        String is = isFailed() ? "NO" : "YES";
        return String.format("%s %s %.2f", this.getIndex(), is, this.summaryPoints());
    }
}

class LabExercises{
    List<Student2> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent (Student2 student){
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n){
        if(ascending){
            students.stream().
                    sorted(Comparator.comparing(Student2::summaryPoints).thenComparing(Student2::getIndex)).
                    limit(n).
                    forEach(System.out::println);
        }
        else{
            students.stream().
                    sorted(Comparator.comparing(Student2::summaryPoints).thenComparing(Student2::getIndex).reversed()).
                    limit(n).
                    forEach(System.out::println);
        }
    }

    public List<Student2> failedStudents (){
        return this.students.stream().filter(Student2::isFailed).
                sorted(Comparator.comparing(Student2::getIndex).thenComparing(Student2::summaryPoints)).
                collect(Collectors.toList());
    }

    public Map<Integer,Double> getStatisticsByYear(){
        return this.students.stream().filter(s -> !s.isFailed()).
                collect(Collectors.groupingBy(
                        Student2::getYear,
                        TreeMap::new,
                        Collectors.averagingDouble(Student2::summaryPoints)
                ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student2(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
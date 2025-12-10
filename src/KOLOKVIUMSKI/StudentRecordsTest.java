//package KOLOKVIUMSKI;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//class Student3{
//    String code;
//    String course;
//    List<Integer> grades;
//
//    public Student3(String code, String course, List<Integer> grades) {
//        this.code = code;
//        this.course = course;
//        this.grades = grades;
//    }
//
//    public static Student3 createStudent(String line){
//        String [] tokens = line.split("\\s+");
//        String code = tokens[0];
//        String course = tokens[1];
//        List<Integer> grades = Arrays.stream(tokens).skip(2).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
//        return new Student3(code, course, grades);
//    }
//    public double getAvg(){
//        return grades.stream().mapToInt(Integer::intValue).average().orElse(0);
//    }
//    public String getCode() {
//        return code;
//    }
//
//    public String getCourse() {
//        return course;
//    }
//
//    public List<Integer> getGrades() {
//        return grades;
//    }
//
//    public int getTens(){
//        return (int) grades.stream().filter( s-> s==10).count();
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s %.2f", code, getAvg());
//    }
//}
//
//class StudentRecords{
//    List<Student3> students;
//    Map<String, List<Student3>> map;
//
//    public StudentRecords() {
//        students = new ArrayList<>();
//    }
//
//    int readRecords(InputStream inputStream){
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        students = reader.lines().map(Student3::createStudent).collect(Collectors.toList());
//        map = students.stream().collect(Collectors.groupingBy(
//                Student3::getCourse,
//                ()-> new TreeMap<>(Comparator.comparing((String key) -> {
//                    return map.get(key).stream().mapToInt(Student3::getTens).sum();
//                }, Comparator.reverseOrder())),
//                Collectors.toList()
//        ));
//        return students.size();
//    }
//
//    public String getDist(List<Student3> students){
//        map.keySet().stream().forEach(k -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append("6 | ");
//            String str = "*".repeat()
//        });
//    }
//
//    void writeTable(OutputStream outputStream){
//        PrintWriter writer = new PrintWriter(outputStream);
//        Map<String, List<Student3>> map = students.stream().collect(
//                Collectors.groupingBy(
//                        Student3::getCourse,
//                        Collectors.toList()
//                )
//        );
//
//        map.keySet().stream().forEach(k -> {
//            List<Student3> list = map.get(k);
//            writer.println(k);
//            list.stream().
//                    sorted(Comparator.comparing(Student3::getAvg, Comparator.reverseOrder()).thenComparing(Student3::getCode)).
//                    forEach(writer::println);
//        });
//
//        writer.flush();
//    }
//
//    void writeDistribution(OutputStream outputStream){
//        PrintWriter writer = new PrintWriter(outputStream);
//
//    }
//}
//
//public class StudentRecordsTest {
//    public static void main(String[] args) {
//        System.out.println("=== READING RECORDS ===");
//        StudentRecords studentRecords = new StudentRecords();
//        int total = studentRecords.readRecords(System.in);
//        System.out.printf("Total records: %d\n", total);
//        System.out.println("=== WRITING TABLE ===");
//        studentRecords.writeTable(System.out);
//        System.out.println("=== WRITING DISTRIBUTION ===");
//        studentRecords.writeDistribution(System.out);
//    }
//}
//
//// your code here
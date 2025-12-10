package KOLOKVIUMSKI;

import java.io.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee{
    protected String level;
    protected String id;
    protected double salary;

    public Employee(String level, String id) {
        this.level = level;
        this.id = id;
        this.salary = 0;
    }

    public String getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }

    abstract void salary(double coef);
}

class HourlyEmployee extends Employee{

    double hours;

    public HourlyEmployee(String level, String id, double hours) {
        super(level, id);
        this.hours = hours;
    }

    @Override
    void salary(double coef) {
        this.salary = hours <= 40 ? hours * coef : 40 * coef + ((hours - 40) * coef) * 1.5;
    }

    double getRegularHours(){
        return hours > 40 ? 40 : hours;
    }
    double getOvertimeHours(){
        return hours < 40 ? 0 : hours - 40;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                id, level, getSalary(), getRegularHours(), getOvertimeHours()
        );
    }
}

class FreelanceEmployee extends Employee{
    List<Integer> tickets;

    public FreelanceEmployee(String level, String id, List<Integer> tickets) {
        super(level, id);
        this.tickets = tickets;
    }


    @Override
    void salary(double coef) {
        int sum = this.tickets.stream().mapToInt(Integer::intValue).sum();
        this.salary = sum * coef;
    }

    int ticketCount(){
        return this.tickets.size();
    }

    int ticketPoints(){
        return this.tickets.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                id, level, getSalary(), ticketCount(), ticketPoints()
        );
    }

}

class PayrollSystem{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    Set<Employee> list;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    void readEmployees (InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        list = reader.lines().map(l -> {
            String [] tokens = l.split(";");
            if(tokens[0].equals("H")){
                HourlyEmployee h = new HourlyEmployee(tokens[2], tokens[1], Double.parseDouble(tokens[3]));
                h.salary(hourlyRateByLevel.get(tokens[2]));
                return h;
            }
            else{
                ArrayList<Integer> tickets = Arrays.stream(tokens).skip(3).
                        map(Integer::parseInt).
                        collect(Collectors.toCollection(ArrayList::new));

                FreelanceEmployee h = new FreelanceEmployee(tokens[2], tokens[1], tickets);
                h.salary(ticketRateByLevel.get(tokens[2]));
                return h;
            }
        }).collect(Collectors.toSet());
    }

    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels) {
        return list.stream().filter(l -> levels.contains(l.getLevel())).
                collect(Collectors.groupingBy(
                        Employee::getLevel,
                        TreeMap::new,
                        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Employee::getSalary).
                                thenComparing(Employee::getLevel).reversed()))
                ));
    }

}

public class PayrollSystemTest {

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        File file = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\KOLOKVIUMSKI\\genericClass");
        payrollSystem.readEmployees(new FileInputStream(file));

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });
    }
}
package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class BonusNotAllowedException extends Exception {
    public BonusNotAllowedException(String message) {
        super(message);
    }
}

abstract class Employee2{
    protected String level;
    protected String id;
    protected double salary;
    protected String bonus;
    double bonusAsDouble;


    public Employee2(String level, String id, String bonus) {
        this.level = level;
        this.id = id;
        this.salary = 0;
        this.bonus = bonus;
        this.bonusAsDouble = 0;
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
    abstract int ticketPoint2();
    abstract double overTimeSalary(double coef);
    abstract int ticketCount();
    abstract String getType();
    void setBonus() throws BonusNotAllowedException {
        if(!bonus.equals("None")) {
            if (bonus.endsWith("%")) {
                double bonusS = Double.parseDouble(bonus.replace("%", ""));
                if (bonusS > 20) throw new BonusNotAllowedException("");
                this.bonusAsDouble = bonusS / 100 * this.getSalary();
            } else {
                double bonusS = Double.parseDouble(bonus);
                if (bonusS > 1000) throw new BonusNotAllowedException("");
                this.bonusAsDouble = Double.parseDouble(bonus);
            }
        }
    };
    double getBonus(){
        return this.bonusAsDouble;
    }

}

class HourlyEmployee2 extends Employee2{

    double hours;

    public HourlyEmployee2(String level, String id, double hours, String bonus) {
        super(level, id, bonus);
        this.hours = hours;
    }

    @Override
    void salary(double coef) {
        this.salary = hours <= 40 ? hours * coef : 40 * coef + ((hours - 40) * coef) * 1.5;
    }

    @Override
    int ticketPoint2() {
        return 0;
    }

    @Override
    double overTimeSalary(double coef) {
        return this.hours <= 40 ? 0 : ((hours - 40) * coef) * 1.5;
    }

    @Override
    int ticketCount() {
        return 0;
    }

    @Override
    String getType() {
        return "H";
    }

    double getRegularHours(){
        return hours > 40 ? 40 : hours;
    }
    double getOvertimeHours(){
        return hours < 40 ? 0 : hours - 40;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f, Bonus: %.2f",
                id, level, getSalary() + getBonus(), getRegularHours(), getOvertimeHours(), getBonus()
        );
    }
}

class FreelanceEmployee2 extends Employee2{
    List<Integer> tickets;

    public FreelanceEmployee2(String level, String id, List<Integer> tickets, String bonus) {
        super(level, id, bonus);
        this.tickets = tickets;
    }


    @Override
    void salary(double coef) {
        int sum = this.tickets.stream().mapToInt(Integer::intValue).sum();
        this.salary = sum * coef;
    }

    @Override
    int ticketPoint2() {
        return this.tickets.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    double overTimeSalary(double coef) {
        return 0;
    }

    int ticketCount(){
        return this.tickets.size();
    }

    @Override
    String getType() {
        return "F";
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d, Bonus: %.2f",
                id, level, getSalary() + getBonus(), ticketCount(), ticketPoint2(), getBonus()
        );
    }

}

class PayrollSystem2{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    Set<Employee2> list;

    public PayrollSystem2(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.list = new HashSet<>();
    }

    Employee2 createEmployee (String line) throws BonusNotAllowedException {
        String [] tokens2 = line.split("\\s+");
        String bonus;
        if (tokens2.length == 1) bonus = "None";
        else bonus = tokens2[1];


        String [] tokens = tokens2[0].split(";");
        if(tokens[0].equals("H")){
            HourlyEmployee2 h = new HourlyEmployee2(tokens[2], tokens[1], Double.parseDouble(tokens[3]), bonus);
            h.salary(hourlyRateByLevel.get(tokens[2]));
            h.setBonus();
            list.add(h);
            return h;
        }
        else{
            ArrayList<Integer> tickets = Arrays.stream(tokens).skip(3).
                    map(Integer::parseInt).
                    collect(Collectors.toCollection(ArrayList::new));

            FreelanceEmployee2 h = new FreelanceEmployee2(tokens[2], tokens[1], tickets, bonus);
            h.setBonus();
            list.add(h);
            h.salary(ticketRateByLevel.get(tokens[2]));
            return h;
        }
    }

    Map<String, Double> getOvertimeSalaryForLevels (){
        return list.stream().filter(s -> s.getType().equals("H")).collect(
                Collectors.groupingBy(
                        Employee2::getLevel,
                        Collectors.summingDouble(e -> e.overTimeSalary(hourlyRateByLevel.get(e.getLevel())))
                )
        );
    }

    void printStatisticsForOvertimeSalary (){
        DoubleSummaryStatistics stats = list.stream().filter(e -> e.getType().equals("H")).
                mapToDouble(e -> e.overTimeSalary(hourlyRateByLevel.get(e.getLevel()))).
                summaryStatistics();
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        System.out.println("Sum: " + stats.getSum());
        System.out.println("Average: " + stats.getAverage());

    }

    Map<String, Integer> ticketsDoneByLevel(){
        return list.stream().filter(e -> e.getType().equals("F")).collect(
                Collectors.groupingBy(
                        Employee2::getLevel,
                        Collectors.summingInt(Employee2::ticketCount)
                )
        );
    }

    Collection<Employee2> getFirstNEmployeesByBonus (int n){
        return list.stream().sorted(Comparator.comparing(Employee2::getBonus).reversed()).limit(n).
                collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Employee2::getBonus).reversed())));
    }
}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem2 ps = new PayrollSystem2(hourlyRateByLevel, ticketRateByLevel);
        Employee2 emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}
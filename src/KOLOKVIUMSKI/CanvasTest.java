package KOLOKVIUMSKI;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super(String.format("ID %s is not valid", message));
    }
}


class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}



abstract class Shape2{
    List<Double> sides;

    public Shape2(List<Double> sides) {
        this.sides = sides;
    }

    public static boolean isAlphaNum(String str){
        return str.length()==6 && IntStream.range(0, str.length()).
                allMatch( s-> Character.isLetter(str.charAt(s)) || Character.isDigit(str.charAt(s)) );
    }

    public static void isOkay(List<Double> sides) throws InvalidDimensionException {
        if(sides.contains(0.0)){
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
    }

    abstract double perimeter();
    abstract double area();
    void scale(double c){
        this.sides = this.sides.stream().map(x -> x*c).collect(Collectors.toList());
    };

    public static Shape2 createShape(String line) throws InvalidIDException, InvalidDimensionException {
        String [] tokens = line.split(" ");
        String type = tokens[0];
        String id = tokens[1];
        if(!Shape2.isAlphaNum(id)){
            throw new InvalidIDException(id);
        }

        if(tokens.length == 4){
            List<Double> sides = List.of(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
            isOkay(sides);
            return new Rectangle2(sides);
        }
        else{
            List<Double> sides = List.of(Double.parseDouble(tokens[2]));
            isOkay(sides);
            if(type.equals("1")){
                return new Circle2(sides);
            }
            else{
                return new Square2(sides);
            }
        }
    }
}

class Square2 extends Shape2{

    public Square2(List<Double> sides) {
        super(sides);
    }

    @Override
    double perimeter() {
        double side = sides.get(0);
        return 4*side;
    }

    @Override
    double area() {
        double side = sides.get(0);
        return side*side;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",
                sides.get(0), this.area(), this.perimeter());
    }

}

class Rectangle2 extends Shape2{
    public Rectangle2(List<Double> sides) {
        super(sides);
    }

    @Override
    double perimeter() {
        double a = sides.get(0);
        double b = sides.get(1);
        return 2*a + 2*b;
    }

    @Override
    double area() {
        double a = sides.get(0);
        double b = sides.get(1);
        return a * b;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",
                sides.get(0), sides.get(1), this.area(), this.perimeter());
    }
}

class Circle2 extends Shape2{
    public Circle2(List<Double> sides) {
        super(sides);
    }

    @Override
    double perimeter() {
        double radius = sides.get(0);
        return 2 * Math.PI * radius;
    }

    @Override
    double area() {
        double radius = sides.get(0);
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",
                sides.get(0), this.area(), this.perimeter());
    }
}

class Canvas{

    Map<String, Set<Shape2>> map;
    Set<Shape2> shapes;

    public Canvas() {
        this.shapes = new TreeSet<>(Comparator.comparing(Shape2::area));
        this.map = new HashMap<>();
    }

    public void readShapes(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> lines = reader.lines().collect(Collectors.toList());

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String id = line.split(" ")[1];
            try{
                Shape2 shape= Shape2.createShape(line);
                this.map.putIfAbsent(id,new TreeSet<>(Comparator.comparing(Shape2::perimeter)));

                this.map.get(id).add(shape);
                this.shapes.add(shape);
            }catch (InvalidIDException ex){
                System.out.println(ex.getMessage());
            }catch (InvalidDimensionException ex){
                System.out.println(ex.getMessage());
                break;
            }
        }
    }

    public void printAllShapes(OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        writer.println(this.shapes.stream().map(Shape2::toString).collect(Collectors.joining("\n")));
        writer.flush();
    }

    public void scaleShapes(String number, double v) {
        if (!map.containsKey(number)) return;
        map.get(number).forEach(s -> s.scale(v));
    }

    public void printByUserId(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        this.map.entrySet().stream()
                .sorted(Comparator
                        .comparing((Map.Entry<String, Set<Shape2>> e) -> e.getValue().size()).reversed()
                        .thenComparing((Map.Entry<String, Set<Shape2>> e) ->
                                e.getValue().stream().mapToDouble(Shape2::area).sum())
                )
                .forEach(entry -> {
                    String userId = entry.getKey();
                    Set<Shape2> shapesSet = entry.getValue();

                    writer.println("Shapes of user: " + userId);
                    shapesSet.forEach(shape -> writer.println(shape.toString()));
                });
        writer.flush();
    }

    public void statistics(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
        DoubleSummaryStatistics summaryStatistics = this.shapes.stream().map(Shape2::area).
                mapToDouble(Double::doubleValue).
                summaryStatistics();
        writer.println(String.format("count: %d", summaryStatistics.getCount()));
        writer.println(String.format("sum: %.2f", summaryStatistics.getSum()));
        writer.println(String.format("min: %.2f", summaryStatistics.getMin()));
        writer.println(String.format("average: %.2f", summaryStatistics.getAverage()));
        writer.println(String.format("max: %.2f", summaryStatistics.getMax()));
        writer.flush();
    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
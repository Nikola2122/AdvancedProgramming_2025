package KOLOKVIUMSKI;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class InvalidCanvasException extends RuntimeException {
    public InvalidCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
    }
}

abstract class Shape{
    double dimension;

    public Shape(double dimension) {
        this.dimension = dimension;
    }

    abstract double area();
    abstract String type();
}

class Square extends Shape{

    public Square(double dimension) {
        super(dimension);
    }

    @Override
    double area() {
        return dimension*dimension;
    }

    @Override
    String type() {
        return "s";
    }
}

class Circle extends Shape{
    public Circle(double dimension) {
        super(dimension);
    }

    double area(){
        return Math.PI*dimension*dimension;
    }

    @Override
    String type() {
        return "c";
    }
}

class Window implements Comparable<Window> {
    String id;
    List<Shape> shapes;

    public Window(String id) {
        this.id = id;
        shapes = new ArrayList<>();
    }

    void addShape(Shape shape){
        if(shape.area() > ShapesApplication.maxArea){
            throw new InvalidCanvasException(id, ShapesApplication.maxArea);
        }
        shapes.add(shape);
    }

    int numShapes(){
        return shapes.size();
    }

    int numCircles(){
        return (int) shapes.stream().filter(s -> s.type().equals("c")).count();
    }

    int numSquares(){
        return (int) shapes.stream().filter(s -> s.type().equals("s")).count();
    }

    double maxArea(){
        return shapes.stream().mapToDouble(s -> s.area()).max().orElse(0);
    }

    double minArea(){
        return shapes.stream().mapToDouble(Shape::area).min().orElse(0);
    }

    double avgArea(){
        return shapes.stream().mapToDouble(s -> s.area()).average().orElse(0);
    }

    double sumArea(){
        return shapes.stream().mapToDouble(s -> s.area()).sum();
    }


    @Override
    public String toString() {
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                numShapes(),
                numCircles(),
                numSquares(),
                minArea(),
                maxArea(),
                avgArea());
    }

    @Override
    public int compareTo(Window o) {
        return Double.compare(o.sumArea(), this.sumArea());
    }
}
class ShapesApplication{

    static double maxArea;
    List<Window> windows;

    ShapesApplication(double maxArea){
        ShapesApplication.maxArea = maxArea;
        windows = new ArrayList<>();
    }

    void readCanvases (InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> list = reader.lines().collect(Collectors.toList());

        for(String l : list){
            String [] tokens = l.split(" ");

            Window window = new Window(tokens[0]);
            boolean flag = true;
            for (int i = 1; i < tokens.length; i+=2) {
                if(tokens[i].equals("C")){
                    try {
                        window.addShape(new Circle(Double.parseDouble(tokens[i + 1])));
                    }
                    catch (InvalidCanvasException e) {
                        System.out.println(e.getMessage());
                        flag = false;
                    }
                }
                else{
                    try {
                        window.addShape(new Square(Double.parseDouble(tokens[i + 1])));
                    }
                    catch (InvalidCanvasException e) {
                        System.out.println(e.getMessage());
                        flag = false;
                    }
                }
            }
            if(flag){
                windows.add(window);
            }
        }
    }

    void printCanvases (OutputStream outputStream){
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
        String str = windows.stream().sorted().map(Window::toString).collect(Collectors.joining("\n"));
        writer.println(str);
        writer.flush();
    }
}


public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
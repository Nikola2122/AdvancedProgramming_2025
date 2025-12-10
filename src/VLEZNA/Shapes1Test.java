package VLEZNA;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class Shape implements Comparable<Shape> {
    String name;
    int number;
    int perimeter;

    public Shape(String name, int number, int perimeter) {
        this.name = name;
        this.number = number;
        this.perimeter = perimeter;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", name, number, perimeter);
    }

    @Override
    public int compareTo(Shape o) {
        return Integer.compare(this.perimeter, o.perimeter);
    }
}
class ShapesApplication{

    List<Shape> shapes;

    ShapesApplication(){
    }

    int readCanvases (InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.shapes = reader.lines().map(l ->{
            String [] tokens = l.split(" ");
            int sum = Arrays.stream(tokens).skip(1).mapToInt(Integer::parseInt).map(i -> i * 4).sum();
            return new Shape(tokens[0], tokens.length - 1, sum);
        }).toList();

        return shapes.stream().mapToInt(l -> l.number).sum();
    }

    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
        writer.println(this.shapes.stream().max(Shape::compareTo).get());
        writer.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
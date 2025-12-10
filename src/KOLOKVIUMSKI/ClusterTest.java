package KOLOKVIUMSKI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface hasDistance{
    long identity();
    double distance(hasDistance a);
}



class Cluster<T extends hasDistance> {
    List<T> elements;

    public Cluster() {
        this.elements = new ArrayList<>();
    }

    public void addItem(T element){
        elements.add(element);
    }

    void near(long id, int top){
        T el = elements.stream().filter(i -> i.identity() == id).findFirst().get();
        List<T> els = elements.stream()
                .filter(s -> s.identity() != id)
                .sorted(Comparator.comparingDouble(e -> e.distance(el)))
                .limit(top).collect(Collectors.toList());
        IntStream.range(0, els.size()).forEach(i-> System.out.printf("%d. %s %.3f%n", i+1, els.get(i), els.get(i).distance(el)));
    }
}
class Point2D implements hasDistance{
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long identity() {
        return id;
    }

    @Override
    public double distance(hasDistance a) {
        Point2D other = (Point2D) a;
        return Math.sqrt(((x - other.x) * (x - other.x)) + ((y - other.y) * (y - other.y)));
    }

    @Override
    public String toString() {
        return String.format("%d ->",id );
    }
}
/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here
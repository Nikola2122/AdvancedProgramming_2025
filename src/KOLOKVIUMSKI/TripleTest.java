package KOLOKVIUMSKI;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Triple<T extends Number & Comparable<T>>{
    List<T> nums;
    public Triple() {
    }

    public Triple(T first, T second, T third) {
        this.nums = new ArrayList<T>();
        nums.add(first);
        nums.add(second);
        nums.add(third);
    }

    public double max() {
        return nums.stream().mapToDouble(Number::doubleValue).max().orElse(0);
    }
    public double avarage(){
        return nums.stream().mapToDouble(Number::doubleValue).average().orElse(0);
    }

    void sort(){
        nums = nums.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", nums.get(0).doubleValue(), nums.get(1).doubleValue(), nums.get(2).doubleValue());
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple



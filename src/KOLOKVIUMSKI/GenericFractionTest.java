package KOLOKVIUMSKI;
import java.util.Scanner;

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number,U extends Number>{
    double numerator;
    double denominator;

    GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0) throw new ZeroDenominatorException("Denominator cannot be zero");
        this.numerator = numerator.doubleValue();
        this.denominator = denominator.doubleValue();
    }

    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double divider = Math.max(this.denominator, gf.denominator);
        while(divider % this.denominator != 0 || divider % gf.denominator != 0){
            divider++;
        }
        double firstNumerator = divider / this.denominator * this.numerator;
        double secondNumerator = divider / gf.denominator * gf.numerator;

        double sum = firstNumerator + secondNumerator;
        return new GenericFraction<>(sum, divider);
    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f", numerator, denominator);
    }

    double toDouble(){
        return numerator/denominator;
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде

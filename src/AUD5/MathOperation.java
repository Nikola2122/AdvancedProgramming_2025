package AUD5;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;

public class MathOperation {
    public static Double statistics(ArrayList<? extends Number> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble(Number::doubleValue).summaryStatistics();

        double middleStd = list.stream().mapToDouble(Number::doubleValue).
                reduce(0,(result,value)->{
                    return result + (value - summaryStatistics.getAverage()) * (value - summaryStatistics.getAverage());
                });

        return Math.sqrt(middleStd / list.size());
    }

    public static void main(String[] args) {

    }
}

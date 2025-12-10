package AUD1;

import java.util.Arrays;

public class Matrix {
    public static double sum(double [][] matrix){
        return Arrays.stream(matrix).flatMapToDouble(Arrays::stream).sum();
    }

    public static double avg(double [][] matrix){
        return sum(matrix)/(matrix.length * matrix[0].length);
    }


    public static void main(String[] args) {
        double [][] matrix = {{6,2},{1,1},{1,1}};
        System.out.println(sum(matrix));
        System.out.println(avg(matrix));
    }
}

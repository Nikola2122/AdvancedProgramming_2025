package AUD1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class DividableSumOfDigits {
    static int num(int start, int end){
        return (int) IntStream.rangeClosed(start,end).filter((elem)->{
            int sum = Arrays.stream(Integer.toString(elem).split("")).mapToInt(Integer::parseInt).sum();
            return sum != 0 && elem % sum == 0;
        }).count();
    }
    public static void main(String[] args) {
        int num = num(1,10);
        System.out.println(num);
    }
}

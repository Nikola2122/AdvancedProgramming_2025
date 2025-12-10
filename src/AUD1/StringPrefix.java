package AUD1;

import java.util.Scanner;
import java.util.stream.IntStream;

public class StringPrefix {

    static boolean prefixCheck(String str1, String str2){
        if (str2.length()<=str1.length()){
            return IntStream.range(0, str2.length()).allMatch((i)-> str2.charAt(i) == str1.charAt(i));
        }
        else{
            return false;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prefixCheck("banana","banan"));
        IntStream.range(0,11).filter( i -> i%2 != 0).forEach(i -> System.out.print(i + " "));
    }
}

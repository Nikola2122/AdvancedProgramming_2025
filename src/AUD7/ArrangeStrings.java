package AUD7;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ArrangeStrings {

    public static String arrangeWord(String word){
        return word.chars().mapToObj(i -> ((char)i)).sorted().map(String::valueOf).
                collect(Collectors.joining());
    }
    public static String arrangeSentence(String sentence){
        return Arrays.stream(sentence.split("\\s+")).
                map(ArrangeStrings::arrangeWord).sorted().collect(Collectors.joining(" "));
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String sentence = sc.nextLine();
        System.out.println(arrangeSentence(sentence));
    }
}

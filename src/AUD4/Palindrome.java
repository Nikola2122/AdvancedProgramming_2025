package AUD4;

import java.io.*;
import java.util.Scanner;
import java.util.stream.IntStream;


class StringWrapper implements Comparable<StringWrapper> {
    private String str;

    public StringWrapper(String str) {
        this.str = str;
    }

    @Override
    public int compareTo(StringWrapper o) {
        if (this.str.length() != o.str.length())
            return this.str.length() - o.str.length();
        else
            return this.str.compareTo(o.str);
    }

    public String getStr() {
        return str;
    }

}

public class Palindrome {

    private static boolean isPalindrome(String str) {
        return IntStream.range(0, str.length() / 2).allMatch(i->str.charAt(i) == str.charAt(str.length() - i - 1));
    }

    public static StringWrapper getPalindrome(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().filter(Palindrome::isPalindrome).map(StringWrapper::new).max(StringWrapper::compareTo).orElse(null);
    }

    public static void main(String[] args) throws IOException {
        File f = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\Palindrome.txt");
        String str = getPalindrome(new FileInputStream(f)).getStr();
        System.out.println(str);
    }
}

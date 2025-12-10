import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface s<t>{
    void print(t a);
}

class c<t extends Comparable<t>> implements s<String>, Comparable<c<t>>{

    t elem;
    @Override
    public void print(String a) {

    }

    @Override
    public int compareTo(c<t> o) {
        return 0;
    }
}
class clas implements Comparable<clas>{
    int a;
    String b;
    public clas(int a, String b) {
        this.a = a;
        this.b = b;
    }
    @Override
    public int compareTo(clas a) {
        return this.a - a.a;
    }
}
// 1234556

public class Main {

    public static void main(String[] args) {
        int [] arr = {1,2,3,4,5};
        int [] cpp = Arrays.copyOf(arr, 2);
        System.out.println(Arrays.toString(cpp));
    }
}
package KOLOKVIUMSKI;

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    T min;
    T max;
    int updated;

    MinMax(){
        this.updated = 0;
        this.min = null;
        this.max = null;
    }

    void update(T element){
        if(this.min==null && this.max==null) {
            this.min = this.max = element;
            return;
        }

        if(this.min.compareTo(element) != 0 && this.max.compareTo(element) != 0) {
            this.updated++;
        }

        if(element.compareTo(this.min)<0){
            this.min = element;
        }
        if(element.compareTo(this.max)>0){
            this.max = element;
        }
    }

    T max(){
        return max;
    }
    T min(){
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min.toString(), max.toString(), updated);
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class Names{
    Map<String, Integer> names;

    Names(){
        names = new TreeMap<>();
    }
    void addName(String name){
        names.putIfAbsent(name, 0);
        names.put(name, names.get(name) + 1);
    }
    public void printN(int n){
        names.keySet().stream().filter(s->names.get(s) >= n).forEach(s -> {
            String [] arr = s.split("");
            int letters = (int) Arrays.stream(arr).distinct().count();

            System.out.println(String.format("%s (%d) %d", s, names.get(s), letters));
        });
    }

    public String findName(int len, int x){
        List<String> list = names.keySet().stream()
                .filter(s -> s.length() >= len)
                .sorted()
                .collect(Collectors.toList());
        if(list.isEmpty()) return null; // no matching name
        // adjust x to 0-based and fit in list
        x = (x - 1) % list.size();
        return list.get(x);
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde
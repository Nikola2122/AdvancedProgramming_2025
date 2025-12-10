package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>> {
    private List<List<T>> blocks;
    private int n;

    public BlockContainer(int n) {
        this.n = n;
        this.blocks = new ArrayList<>();
    }

    public void add(T a) {
        if (blocks.isEmpty()) {
            blocks.add(new ArrayList<>());
            blocks.get(0).add(a);
        } else {
            if (blocks.get(blocks.size() - 1).size() < n) {
                blocks.get(blocks.size() - 1).add(a);
            } else {
                blocks.add(new ArrayList<>());
                blocks.get(blocks.size() - 1).add(a);
            }
        }
        Collections.sort(blocks.get(blocks.size() - 1));
    }

    public boolean remove(T a) {
        blocks.get(blocks.size() - 1).remove(a);
        if (blocks.get(blocks.size() - 1).isEmpty()) {
            blocks.remove(blocks.size() - 1);
            return true;
        }
        return false;
    }

    public void sort() {
        List<T> all = blocks.stream()
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.toList());
        blocks.clear();
        for (int i = 0; i < all.size(); i += n) {
            int end = Math.min(i + n, all.size());
            blocks.add(new ArrayList<>(all.subList(i, end)));
        }
    }

    @Override
    public String toString() {
        return blocks.stream().map(List::toString).collect(Collectors.joining(","));
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}
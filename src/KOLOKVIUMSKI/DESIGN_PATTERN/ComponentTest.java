package KOLOKVIUMSKI.DESIGN_PATTERN;

import java.util.*;
import java.util.stream.Collectors;

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(String.format("Invalid position %s, alredy taken!", message));
    }
}
interface IComponent extends Comparable<IComponent> {
    void addComponent(Component component);
    String toString(int level);
    int getWeight();
    String getColor();
    void setColor(String color, int weight);
}

class Component implements IComponent {
    String color;
    int weight;
    Set<IComponent> components;

    public Component(String name, int weight) {
        this.color = name;
        this.weight = weight;
        this.components = new TreeSet<>();
    }

    @Override
    public void addComponent(Component component) {
        this.components.add(component);
    }

    @Override
    public String toString(int level) {
        String indent = "---".repeat(level);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%d:%s\n", indent, getWeight(), getColor()));
        sb.append(components.stream().map(c -> c.toString(level+1)).collect(Collectors.joining()));

        return sb.toString();
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color, int weight) {
        if(this.weight < weight) {
            this.color = color;
        }
        this.components.forEach(c -> c.setColor(color, weight));
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(IComponent o) {
        int cmp = Integer.compare(this.getWeight(), o.getWeight());
        if(cmp != 0) return cmp;
        else{
            return this.color.compareTo(o.getColor());
        }
    }
}

class Window{
    String name;
    Map<Integer, IComponent> components;

    public Window(String name){
        this.name = name;
        components = new TreeMap<>();
    }


    public void addComponent(int position, IComponent component) throws InvalidPositionException {
        if(components.containsKey(position)){
            throw new InvalidPositionException(Integer.toString(position));
        }
        else{
            this.components.put(position, component);
        }
    }

    void changeColor(int weight, String color){
        components.values().forEach(c -> c.setColor(color, weight));
    }

    void swichComponents(int pos1, int pos2){
        IComponent component = components.get(pos1);
        IComponent comp = components.get(pos2);
        components.put(pos1, comp);
        components.put(pos2, component);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WINDOW %s\n", name));
        components.forEach((k, v) -> {
            sb.append(String.format("%d:%s", k, v.toString(0)));
        });

        return sb.toString();
    }
}


public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
package AUD5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

interface Drawable<E>{
    E draw();
}

class Circle implements Drawable<Circle>{
    private int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public Circle draw() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%d", radius);
    }
}

public class Box<E extends Drawable<E>> {
    List<E> elements;
    public static Random random = new Random();

    public Box() {
        elements = new ArrayList<E>();
    }

    public void add(E e) {
        elements.add(e);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public E drawElement(){
        if (isEmpty()){
            return null;
        }
        return elements.remove(random.nextInt(elements.size()));
    }

    public static void main(String[] args) {
        Box<Circle> box = new Box<Circle>();
        IntStream.range(0, 10).forEach(i->box.add(new Circle(i)));
        IntStream.range(0, 11).forEach(i-> System.out.println(box.drawElement()));
    }
}

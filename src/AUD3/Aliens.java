package AUD3;

import java.util.ArrayList;
import java.util.List;

abstract class Alien {
    public int health;
    public String name;
    public Alien(int health, String name) {
        this.health = health;
        this.name = name;
    }

    public abstract int getDamage();
}

class SnakeAlien extends Alien{
    public SnakeAlien(int health, String name) {
        super(health, name);
    }

    @Override
    public int getDamage() {
        return 10;
    }
}
class MarshAlien extends Alien{
    public MarshAlien(int health, String name) {
        super(health, name);
    }

    @Override
    public int getDamage() {
        return 50;
    }
}
public class Aliens {
    private List<Alien> aliens;

    public Aliens() {
        aliens = new ArrayList<Alien>();
        aliens.add(new SnakeAlien(100, "Nikola"));
        aliens.add(new MarshAlien(150, "Meli"));
    }

    public int getSumOfDamage(){
        return aliens.stream().mapToInt(Alien::getDamage).sum();
    }

    public static void main(String[] args) {
        Aliens a = new Aliens();
        System.out.println(a.getSumOfDamage());
    }
}

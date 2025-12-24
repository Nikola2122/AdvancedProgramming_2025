package AUD10_Design_Pattern;

interface Drink{
    int price();
    String desc();
}

class Gin implements Drink{

    @Override
    public int price() {
        return 100;
    }
    @Override
    public String desc() {
        return "Gin";
    }
}

abstract class DrinkDecorator implements Drink{
    Drink d;
    public DrinkDecorator(Drink d){
        this.d = d;
    }
}

class Add20PriceDecorator extends DrinkDecorator{
    public Add20PriceDecorator(Drink d){
        super(d);
    }

    @Override
    public int price() {
        return d.price() + 20;
    }

    @Override
    public String desc() {
        return d.desc() + " + 20";
    }
}

class add50PriceDecorator extends DrinkDecorator{
    public add50PriceDecorator(Drink d){
        super(d);
    }
    @Override
    public int price() {
        return d.price() + 50;
    }
    @Override
    public String desc() {
        return d.desc() + " + 50";
    }
}

public class Decorator {
    public static void main(String[] args) {
        Drink d = new Add20PriceDecorator(new add50PriceDecorator(new Gin()));
        System.out.println(d.desc());
        System.out.println(d.price());
    }
}

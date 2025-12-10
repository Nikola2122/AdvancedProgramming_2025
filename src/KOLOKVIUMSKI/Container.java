package KOLOKVIUMSKI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface WeightAble extends Comparable<WeightAble> {
    double getWeight();
}

class WeighAbleString implements WeightAble {

    String string;

    public WeighAbleString(String string) {
        this.string = string;
    }

    @Override
    public double getWeight() {
        return string.length();
    }

    @Override
    public int compareTo(WeightAble o) {
        return Double.compare(getWeight(), o.getWeight());
    }

    @Override
    public String toString() {
        return string;
    }
}

class WeighAbleInt implements WeightAble {

    Integer weight;

    public WeighAbleInt(Integer weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(WeightAble o) {
        return Double.compare(getWeight(), o.getWeight());
    }

    @Override
    public String toString() {
        return Integer.toString(weight);
    }
}

public class Container<T extends WeightAble> implements Comparable<Container<? extends WeightAble>> {
    private List<T> list;

    Container(){
        list = new ArrayList<T>();
    }

    void addElement(T el){
        list.add(el);
    }
    List<T> lighterThan(T el){
        return list.stream().filter(e -> e.compareTo(el)<0).toList();
    }
    List<T> between(T a, T b){
        return list.stream().filter(e -> e.compareTo(a)>0 && e.compareTo(b)<0).toList();
    }

    double sumOfAllWeights(){
        return list.stream().mapToDouble(WeightAble::getWeight).sum();
    }

    @Override
    public int compareTo(Container<? extends WeightAble> other) {
        return Double.compare(sumOfAllWeights(), other.sumOfAllWeights());
    }

    public static void main(String[] args) {
        Container<WeighAbleString> c1 = new Container<>();
        c1.addElement(new WeighAbleString("longer"));
        c1.addElement(new WeighAbleString("shorterrrr"));
        c1.addElement(new WeighAbleString("kr"));
        c1.addElement(new WeighAbleString("dddddddddddddd"));
        c1.addElement(new WeighAbleString("a"));
        c1.addElement(new WeighAbleString("aaaaaaaaaaaaaaaaaaaa"));

        List<WeighAbleString> list = c1.lighterThan(new WeighAbleString("ddddddddd"));
        List<WeighAbleString> l2 = c1.between(new WeighAbleString("shorterrrr"), new WeighAbleString("ddddddddddddddd"));
        System.out.println(list);
        System.out.println(l2);
        System.out.println(c1.sumOfAllWeights());

        Container<WeighAbleInt> c2 = new Container<>();
        c2.addElement(new WeighAbleInt(2));
        c2.addElement(new WeighAbleInt(0));
        c2.addElement(new WeighAbleInt(0));
        c2.addElement(new WeighAbleInt(0));
        c2.addElement(new WeighAbleInt(51));

        System.out.println(c1.compareTo(c2));
    }
}

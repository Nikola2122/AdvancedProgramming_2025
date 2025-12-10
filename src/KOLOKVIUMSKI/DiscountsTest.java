package KOLOKVIUMSKI;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Discounts
 */
class Product{
    double priceWithDiscount;
    double price;

    public Product(double priceWithDiscount, double price) {
        this.priceWithDiscount = priceWithDiscount;
        this.price = price;
    }

    int percentDiscount(){
        double d = priceWithDiscount/price * 100;
        double m = (double)((int) d);
        if(m == d){
            return 100 - (int) d;
        }

        return 100 - (int) d - 1;
    }

    double absoluteDiscount(){
        return price - priceWithDiscount;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", percentDiscount(), (int)priceWithDiscount, (int)price);
    }
}

class Store{
    String name;
    List<Product> products;

    public Store(String name, List<Product> products) {
        this.products = products;
        this.name = name;
    }


    public static Store makeStore(String line){
        String [] array = line.split("\\s+");
        List<Product> products = Arrays.stream(array).skip(1).map(str -> {
            String [] tokens = str.split(":");
            return new Product(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]));
        }).collect(Collectors.toList());

        return new Store(array[0], products);
    }

    double averageDiscount(){
        return this.products.stream().mapToInt(Product::percentDiscount).average().orElse(0);
    }

    public String getName() {
        return name;
    }

    int totalDiscount(){
        return (int) this.products.stream().mapToDouble(Product::absoluteDiscount).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("\n");
        sb.append(String.format("Average discount: %.1f%%", averageDiscount())).append("\n");
        sb.append(String.format("Total discount: %d", totalDiscount())).append("\n");

        sb.append(this.products.stream().
                sorted(Comparator.comparing(Product::percentDiscount).thenComparing(Product::absoluteDiscount).reversed()).
                map(Product::toString).collect(Collectors.joining("\n")));
        return sb.toString();
    }
}

class Discounts{

    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        stores = reader.lines().map(Store::makeStore).collect(Collectors.toList());

        return stores.size();
    }

    public List<Store> byAverageDiscount(){
        return this.stores.stream().
                sorted(Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName).reversed()).
                limit(3).
                collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount(){
        return this.stores.stream().
                sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName)).
                limit(3).
                collect(Collectors.toList());
    }

}
public class DiscountsTest {
    public static void main(String[] args) throws FileNotFoundException {
        Discounts discounts = new Discounts();
        File f = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\KOLOKVIUMSKI\\discountTest");
        int stores = discounts.readStores(new FileInputStream(f));
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
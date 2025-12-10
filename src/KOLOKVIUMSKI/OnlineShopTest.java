package KOLOKVIUMSKI;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(String.format("Product with id %s does not exist in the online shop!", message));
    }
}


class Product2 {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold;

    public Product2(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.quantitySold = 0;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold+=quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {
    Map<String, Product2> map;
    double totalRevenue;

    OnlineShop() {
        map = new HashMap<>();
        totalRevenue = 0;
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product2 p2 = new Product2(category, id, name, createdAt, price);
        map.put(id, p2);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(!map.containsKey(id)){
            throw new ProductNotFoundException(id);
        }
        Product2 p2 = map.get(id);
        p2.setQuantitySold(quantity);
        totalRevenue += p2.getPrice() * quantity;
        return p2.getPrice() * quantity;
    }

    List<List<Product2>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product2> result;
        if(category != null){
            result = map.values().stream().filter(s -> s.getCategory().equals(category)).collect(Collectors.toList());
        }
        else{
            result = new ArrayList<>(map.values());
        }
        if(comparatorType == COMPARATOR_TYPE.NEWEST_FIRST){
            result = result.stream().sorted(Comparator.comparing(Product2::getCreatedAt).reversed()).collect(Collectors.toList());
        }
        else if(comparatorType == COMPARATOR_TYPE.OLDEST_FIRST){
            result = result.stream().sorted(Comparator.comparing(Product2::getCreatedAt)).collect(Collectors.toList());
        }
        else if(comparatorType == COMPARATOR_TYPE.LOWEST_PRICE_FIRST){
            result = result.stream().sorted(Comparator.comparing(Product2::getPrice)).collect(Collectors.toList());
        }
        else if(comparatorType == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST){
            result = result.stream().sorted(Comparator.comparing(Product2::getPrice).reversed()).collect(Collectors.toList());

        }
        else if(comparatorType == COMPARATOR_TYPE.MOST_SOLD_FIRST){
            result = result.stream().sorted(Comparator.comparing(Product2::getQuantitySold).reversed()).collect(Collectors.toList());

        }
        else {
            result = result.stream().sorted(Comparator.comparing(Product2::getQuantitySold)).collect(Collectors.toList());

        }
        List<List<Product2>> result2 = new ArrayList<>();
        for (int i = 0; i < result.size(); i += pageSize) {
            int end = Math.min(i + pageSize, result.size());
            result2.add(result.subList(i, end));
        }

        return result2;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product2>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


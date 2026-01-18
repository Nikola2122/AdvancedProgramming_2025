package KOLOKVIUMSKI.DESIGN_PATTERN;

import java.util.*;

class DeliveryApp{
    private String name;
    private Map<String, DeliveryPerson> deliverers;
    private Map<String, Restaurant> restaurants;
    private Map<String, User> users;

    public DeliveryApp(String name) {
        this.name = name;
        deliverers = new HashMap<>();
        restaurants = new HashMap<>();
        users = new HashMap<>();
    }

    public void registerDeliveryPerson(String id, String name, Location location) {
        this.deliverers.put(id, new DeliveryPerson(id, name, location));
    }

    public void addRestaurant(String id, String name, Location location) {
        this.restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addUser(String id, String name) {
        this.users.put(id, new User(id, name));
    }

    public void addAddress (String id, String addressName, Location location){
        users.get(id).addAddress(addressName, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        User user = users.get(userId);
        Restaurant restaurant = restaurants.get(restaurantId);
        Location userLocation = user.getLocation(userAddressName);
        DeliveryPerson deliveryPerson = deliverers.values().stream().
                min(Comparator.comparing((DeliveryPerson d) -> d.location.distance(restaurant.location)).
                        thenComparing((DeliveryPerson d) -> d.numberOfDeliveries)).orElse(null);
        user.totalSpent += cost;
        restaurant.totalProfit += cost;
        user.totalOrders++;
        restaurant.totalOrders++;
        if(deliveryPerson != null){
            deliveryPerson.numberOfDeliveries++;
            int distance = restaurant.location.distance(deliveryPerson.location);
            int extra = (distance / 10) * 10;

            deliveryPerson.location = LocationCreator.create(userLocation.getX(), userLocation.getY());


            deliveryPerson.totalDeliveryFee += 90 + extra;
            deliveryPerson.location = LocationCreator.create(userLocation.getX(), userLocation.getY());
        }
    }

    public void printUsers() {
        users.values().stream().sorted(Comparator.comparing(User::getTotalSpent, Comparator.reverseOrder()).
                        thenComparing(User::getName, Comparator.reverseOrder()))
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream().sorted(Comparator.comparing(Restaurant::average, Comparator.reverseOrder()).
                        thenComparing(Restaurant::getName, Comparator.reverseOrder()))
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliverers.values().stream().sorted(Comparator.comparing(DeliveryPerson::getTotalDeliveryFee, Comparator.reverseOrder()).
                        thenComparing(DeliveryPerson::getName, Comparator.reverseOrder()))
                .forEach(System.out::println);
    }
}

class DeliveryPerson{
    private String id;
    private String name;
    Location location;
    float totalDeliveryFee;
    int numberOfDeliveries;

    public DeliveryPerson(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalDeliveryFee = 0f;
        this.numberOfDeliveries = 0;
    }

    float average(){
        return numberOfDeliveries != 0 ?  totalDeliveryFee / numberOfDeliveries : 0;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, numberOfDeliveries, totalDeliveryFee, average());
    }

    public float getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public String getName() {
        return name;
    }
}

class Restaurant {
    private String id;
    private String name;
    Location location;
    float totalProfit;
    int totalOrders;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalProfit = 0f;
        this.totalOrders = 0;
    }

    float average(){
        return totalOrders != 0 ? totalProfit / totalOrders : 0;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, totalOrders,  totalProfit, average());
    }

    public String getName() {
        return name;
    }
}

class User {
    private String id;
    private String name;
    private Map<String, Location> locations;
    float totalSpent;
    int totalOrders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.totalSpent = 0f;
        locations = new HashMap<>();
        this.totalOrders = 0;
    }

    public void addAddress(String addressName, Location location){
        this.locations.put(addressName, location);
    }

    public Location getLocation(String addressName){
        return this.locations.get(addressName);
    }

    float average(){
        return totalOrders != 0 ? totalSpent / totalOrders : 0;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, totalOrders, totalSpent, average());
    }

    float getTotalSpent(){
        return totalSpent;
    }

    public String getName() {
        return name;
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}

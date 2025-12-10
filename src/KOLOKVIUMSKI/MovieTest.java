package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    private String title;
    private String director;
    private String genre;
    private float rating;

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    public Movie(String title, String director, String genre, float rating) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) %.2f", title, director, genre, rating);
    }

}

class MoviesCollection{
    Map<String, Integer> map;
    List<Movie> movies;
    public MoviesCollection(){
        movies = new ArrayList<>();
        map = new HashMap<>();
    }

    public void addMovie(Movie movie){
        movies.add(movie);
        map.putIfAbsent(movie.getDirector(), 0);
        map.put(movie.getDirector(), map.get(movie.getDirector()) + 1);
    }

    public void printByGenre(String genre){
        String str = movies.stream().filter(s -> s.getGenre().equalsIgnoreCase(genre)).
                sorted(Comparator.comparing(Movie::getTitle).thenComparing(Movie::getRating)).
                map(Movie::toString).
                collect(Collectors.joining("\n"));
        System.out.println(str);
    }
    public List<Movie> getTopRatedN(int n){
        return n >= movies.size() ? movies : movies.stream().
                sorted(Comparator.comparing(Movie::getRating).reversed()).
                limit(n).collect(Collectors.toList());
    }

    public Map<String, Integer> getCountByDirector(int n){
        return map.keySet().stream().sorted().limit(n).
                collect(Collectors.toMap(
                        i -> i,
                        i -> map.get(i),
                        (v1, v2) -> v1,
                        TreeMap::new
                ));
    }
}


public class MovieTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int printN = scanner.nextInt();
        scanner.nextLine();
        MoviesCollection moviesCollection = new MoviesCollection();
        Set<String> genres = fillCollection(scanner, moviesCollection, n);
        System.out.println("=== PRINT BY GENRE ===");
        for (String genre : genres) {
            System.out.println("GENRE: " + genre);
            moviesCollection.printByGenre(genre);
        }
        System.out.println("=== TOP N BY RATING ===");
        printList(moviesCollection.getTopRatedN(printN));

        System.out.println("=== COUNT BY DIRECTOR ===");
        printMap(moviesCollection.getCountByDirector(n));


    }

    static void printMap(Map<String,Integer> countByDirector) {
        countByDirector.entrySet().stream()
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    static void printList(List<Movie> movies) {
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          MoviesCollection collection, int n) {
        TreeSet<String> categories = new TreeSet<String>();
        while (n>0) {
            n--;
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Movie movie = new Movie(parts[0], parts[1], parts[2], Float.parseFloat(parts[3]));
            collection.addMovie(movie);
            categories.add(parts[2]);
        }
        return categories;
    }
}
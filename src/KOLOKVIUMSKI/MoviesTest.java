package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class Movie2{
    String title;
    List<Integer> ratings;

    public Movie2(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public double averageRating(){
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    public double ratingCoef(int number){
        return averageRating() * ratings.size() / number;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title, averageRating(), ratings.size());
    }
}

class MoviesList{
    List<Movie2> list;
    int maxRatings;

    public MoviesList() {
        list = new ArrayList<Movie2>();
        maxRatings = 0;
    }

    public void addMovie(String title, int[] ratings){
        List<Integer> l = Arrays.stream(ratings).boxed().collect(Collectors.toList());
        Movie2 m = new Movie2(title, l);
        maxRatings = Math.max(maxRatings, ratings.length);
        list.add(m);
    }

    public List<Movie2> top10ByAvgRating(){
        return list.stream().
                sorted(Comparator.comparing(Movie2::averageRating, Comparator.reverseOrder()).thenComparing(Movie2::getTitle)).
                limit(10).collect(Collectors.toList());
    }

    public List<Movie2> top10ByRatingCoef(){
        return list.stream().
                sorted(Comparator.comparing((Movie2 m) -> m.ratingCoef(this.maxRatings)).reversed().thenComparing(Movie2::getTitle)).
                limit(10).collect(Collectors.toList());
    }

}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie2> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie2 movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie2 movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde
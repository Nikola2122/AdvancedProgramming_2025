package LABS.LAB3;

import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Movie{
    private String title;
    private String genre;
    private int year;
    private double averageRating;

    public Movie(String title, String genre, int year, double averageRating) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.averageRating = averageRating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %.2f", title, genre, year, averageRating);
    }
    public static Movie createMovie(String title, String genre, int year, String ratings) {
        return new Movie(title, genre, year,
                Arrays.stream(ratings.split(" ")).mapToDouble(Double::parseDouble).average().orElse(0));
    }
}

class MovieTheater{
    private ArrayList<Movie> movies;

    public MovieTheater() {
        movies = new ArrayList<>();
    }

    public void readMovies(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int numberOf = Integer.parseInt(reader.readLine()) * 4;
        List<String> lines = reader.lines().collect(Collectors.toList());

        movies = IntStream.range(0, numberOf).mapToObj(i -> {
            if(i%4 == 0){
                String title = lines.get(i);
                String genre = lines.get(i+1);
                int year = Integer.parseInt(lines.get(i+2));
                String ratings = lines.get(i+3);
                return Movie.createMovie(title, genre, year, ratings);
            }
            else {
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        movies = movies.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
    }

    public void printByGenreAndTitle(){
        movies.stream().
                sorted(Comparator.comparing(Movie::getGenre).thenComparing(Movie::getTitle)).
                forEach(System.out::println);
    }

    public void printByRatingAndTitle(){
        movies.stream().
                sorted(Comparator.comparing(Movie::getAverageRating).reversed().thenComparing(Movie::getTitle)).
                forEach(System.out::println);
    }
    public void printByYearAndTitle(){
        movies.stream().
                sorted(Comparator.comparing(Movie::getYear).thenComparing(Movie::getTitle)).
                forEach(System.out::println);
    }
    Map<String, List<Movie>> groupByGenre(){
        return this.movies.stream().collect(
                Collectors.groupingBy(
                        Movie::getGenre,
                        TreeMap::new,
                        Collectors.toList()
                )
        );
    }
    Map<String, Double> ratingByGenre(){
        return this.movies.stream().collect(
                Collectors.groupingBy(
                        Movie::getGenre,
                        Collectors.summingDouble(Movie::getAverageRating)
                )
        );
    }
}

public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        try {
            mt.readMovies(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}
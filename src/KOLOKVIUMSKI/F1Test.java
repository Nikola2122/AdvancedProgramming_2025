package KOLOKVIUMSKI;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Time implements Comparable<Time> {
    int minutes;
    int seconds;
    int milliseconds;

    public Time(int minutes, int seconds, int milliseconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    @Override
    public int compareTo(Time o) {
        return Comparator.comparing(Time::getMinutes).
                thenComparing(Time::getSeconds).
                thenComparing(Time::getMilliseconds).compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", minutes, seconds, milliseconds);
    }
}
class F1Racer{
    String name;
    Time time;

    public F1Racer(String name, Time time) {
        this.name = name;
        this.time = time;
    }

    public static F1Racer getRacer(String line){
        String [] arr = line.split("\\s+");
        Time time = Arrays.stream(arr).skip(1).
                map(s -> {
                   String [] tokens = s.split(":");
                   return new Time(Integer.parseInt(tokens[0]),
                           Integer.parseInt(tokens[1]),
                           Integer.parseInt(tokens[2]));
                }).min(Time::compareTo).orElse(null);
        return new F1Racer(arr[0], time);
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%-10s %9s",name, time);
    }
}

class F1Race {
    // vashiot kod ovde

    List<F1Racer> racers;

    F1Race(){
        this.racers = new ArrayList<F1Racer>();
    }


    public void readResults(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        racers = reader.lines().map(F1Racer::getRacer).collect(Collectors.toList());
        racers.sort(Comparator.comparing(F1Racer::getTime));
    }

    public void printSorted(PrintStream out) {
        PrintWriter writer = new PrintWriter(out);
        writer.println(
                IntStream.range(0, racers.size()).mapToObj(i -> {
                    return String.format("%d. %s", i+1, racers.get(i));
                }).collect(Collectors.joining("\n"))
        );
        writer.flush();
    }
}

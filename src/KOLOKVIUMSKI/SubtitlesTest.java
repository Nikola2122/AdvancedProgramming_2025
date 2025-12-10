package KOLOKVIUMSKI;

import java.io.*;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Subtitle {
    static final DateTimeFormatter SRT_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    int order;
    LocalTime from;
    LocalTime to;
    String text;

    public Subtitle(int order, LocalTime from, LocalTime to, String text) {
        this.order = order;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public static Subtitle getSub(String line1, String line2, String line3) {
        String[] times = line2.split(" --> ");
        LocalTime from = LocalTime.parse(times[0], SRT_FORMAT);
        LocalTime to = LocalTime.parse(times[1], SRT_FORMAT);
        return new Subtitle(Integer.parseInt(line1), from, to, line3);
    }

    public void setTime(int ms) {
        from = from.plus(ms, ChronoUnit.MILLIS);
        to = to.plus(ms, ChronoUnit.MILLIS);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s", order, from.format(SRT_FORMAT), to.format(SRT_FORMAT), text);
    }
}

class Subtitles {

    List<Subtitle> subs;

    public int loadSubtitles(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> lines = reader.lines().collect(Collectors.toList());
        this.subs = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String id = lines.get(i);
            String time = lines.get(i+1);
            i+=2;
            StringBuilder text = new StringBuilder();
            while(i < lines.size() && !lines.get(i).isBlank()){
                text.append(lines.get(i)).append("\n");
                i++;
            }
            this.subs.add(Subtitle.getSub(id, time, text.toString()));
        }

        return subs.size();
    }

    public void print() {
        System.out.println(this);
    }

    public void shift(int shift) {
        subs.forEach(s -> s.setTime(shift));
    }

    @Override
    public String toString() {
        return subs.stream().map(Subtitle::toString).collect(Collectors.joining("\n"));
    }
}

public class SubtitlesTest {
    public static void main(String[] args) throws FileNotFoundException {
        Subtitles subtitles = new Subtitles();
        File f = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\KOLOKVIUMSKI\\subs");
        int n = subtitles.loadSubtitles(new FileInputStream(f));
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

package LABS.LAB3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes

class Ad implements Comparable<Ad> {
    String id;
    String category;
    double bidValue;
    double ctr;
    String content;
    double score;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s", id, category, bidValue, ctr*100, content);
    }

    @Override
    public int compareTo(Ad o) {
        return Comparator.comparing(Ad::getBidValue).reversed().thenComparing(Ad::getId).compare(this, o);
    }

    public double getScore() {
        return score;
    }

    public static Ad createAdd(String line){
        if(!line.isEmpty()) {
            String[] tokens = line.split("\\s+");
            String title = tokens[0];
            String category = tokens[1];
            double bidValue = Double.parseDouble(tokens[2]);
            double ctr = Double.parseDouble(tokens[3]);
            String content = Arrays.stream(tokens).skip(4).collect(Collectors.joining(" "));
            return new Ad(title, category, bidValue, ctr, content);
        }
        return null;
    }
}

class AdRequest{
    String id;
    String category;
    double floorBid;
    String keywords;

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (floor=%.2f): %s", id, category, floorBid, keywords);
    }
    public static AdRequest createAddR(String line){
        String [] tokens = line.split(" ");
        String title = tokens[0];
        String category = tokens[1];
        double bidValue = Double.parseDouble(tokens[2]);
        String content = Arrays.stream(tokens).skip(3).collect(Collectors.joining(" "));
        return new AdRequest(title, category, bidValue, content);
    }
}
class AdNetwork {
    public static double x = 5.0;
    public static double y = 100.0;

    private List<Ad> ads;
    private List<String> lines;
    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }

    public void readAds(BufferedReader in) {
        lines = in.lines().collect(Collectors.toList());
        ads = lines.stream().limit(lines.size()-1).map(Ad::createAdd).collect(Collectors.toList());
        ads = ads.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void placeAds(BufferedReader br, int i, PrintWriter pw) throws IOException {
        String line = lines.get(lines.size()-1);
        AdRequest request = AdRequest.createAddR(line);
        List<Ad> left = ads.stream().filter(s -> s.getBidValue() >= request.getFloorBid())
                .collect(Collectors.toList());
        left.stream().forEach(s ->{
            double totalScore = relevanceScore(s, request) + x * s.getBidValue() + y * s.getCtr();
            s.setScore(totalScore);
        });
        left = left.stream().sorted(Comparator.comparing(Ad::getScore).reversed()).limit(i).sorted().collect(Collectors.toList());
        pw.println(String.format("Top ads for request %s:", request.getId()));
        pw.println(left.stream().map(Ad::toString).collect(Collectors.joining("\n")));
        pw.flush();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}
package LABS.LAB4;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


class TermFrequency{

    Map<String, Long> map;



    TermFrequency(InputStream inputStream, String[] stopWords){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> stopSet = Arrays.stream(stopWords).
                map(String::toLowerCase).
                collect(Collectors.toList());

        map = reader.lines()
                .flatMap(l -> {
                    String line = l.replaceAll("\\.", "").replaceAll(",", "");
                    return Arrays.stream(line.split("\\s+")); // split by whitespace
                })
                .map(String::toLowerCase)
                .filter(s -> !(stopSet.contains(s)) && !s.isEmpty())
                .collect(Collectors.groupingBy(
                        s -> s,
                        HashMap::new,
                        Collectors.counting()
                ));
    }

    public int countTotal() {
        return map.values().stream().mapToInt(Long::intValue).sum();
    }

    public int countDistinct() {
        return map.keySet().size();
    }


    public List<String> mostOften(int i) {
        return map.entrySet().stream().sorted(Entry.<String, Long>comparingByValue().reversed().
                thenComparing(Entry::getKey)).
                map(Entry::getKey).
                limit(i).
                collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

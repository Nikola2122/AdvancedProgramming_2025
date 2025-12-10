package VLEZNA;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


class LargestChar{
    Map<Character, Integer> map;

    public LargestChar(String line) {
        this.map = new HashMap<>();
        IntStream.range(0, line.length()).forEach(i->{
            if(map.containsKey(line.charAt(i))){
                map.put(line.charAt(i), map.get(line.charAt(i))+1);
            }
            else{
                map.put(line.charAt(i), 1);
            }
        });
    }

    public int getLargest(){
        return map.values().stream().max(Integer::compareTo).orElse(0);
    }
}
public class Chars {

    public static void processDataAndOutput(InputStream inputStream, OutputStream outputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<LargestChar> list = reader.lines().map(LargestChar::new).toList();
        PrintWriter writer = new PrintWriter(outputStream);
        list.forEach(l->writer.println(l.getLargest()));
        writer.flush();
    }
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\VLEZNA\\Chars.txt");
        processDataAndOutput(new FileInputStream(f),System.out);
    }
}

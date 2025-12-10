package AUD4;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class ConsumerOfLines implements Consumer<String> {
    int numberOfLines;
    int numberOfWords;
    int numberOfCharacters;

    public ConsumerOfLines(){
        numberOfLines = 0;
        numberOfWords = 0;
        numberOfCharacters = 0;
    }
    @Override
    public void accept(String line) {
        numberOfLines++;
        String [] arr = line.split(" ");
        numberOfWords += !(arr[0].equals(" ")) ? arr.length : 0;
        numberOfCharacters += line.length();
    }

    @Override
    public String toString() {
        return String.format("Number of lines: %d, Number of words: %d, Number of characters: %d",
                numberOfLines, numberOfWords, numberOfCharacters);
    }
}

class Lines{
    int numberOfLines;
    int numberOfWords;
    int numberOfCharacters;

    public Lines(int numberOfLines, int numberOfWords, int numberOfCharacters) {
        this.numberOfLines = numberOfLines;
        this.numberOfWords = numberOfWords;
        this.numberOfCharacters = numberOfCharacters;
    }
    public Lines(String line){
        numberOfLines++;
        String [] arr = line.split(" ");
        numberOfWords += !(arr[0].equals(" ")) ? arr.length : 0;
        numberOfCharacters+=line.length();
    }

    public Lines sum(Lines other){
        return new Lines(numberOfLines + other.numberOfLines,
                numberOfWords + other.numberOfWords,
                numberOfCharacters + other.numberOfCharacters);
    }

    @Override
    public String toString() {
        return String.format("Number of lines: %d, Number of words: %d, Number of characters: %d",
                numberOfLines, numberOfWords, numberOfCharacters);
    }
}

public class WordCount {
    public static void ReadFromWithBufferedReaderBad(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        int numberOfLines = 0, numberOfWords = 0, numberOfCharacters = 0;
        String line;

        while (!(line = reader.readLine()).equals("END")) {
            numberOfLines++;
            String [] arr = line.split(" ");
            numberOfWords += !(arr[0].equals(" ")) ? arr.length : 0;
            numberOfCharacters+=line.length();
            System.out.println(line);
        }
        System.out.printf("Number of lines: %d, Number of words: %d, Number of characters: %d%n",
                numberOfLines, numberOfWords, numberOfCharacters);
    }

    public static void ReadFromWithBufferedReader(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Lines lines = reader.lines().
                map(Lines::new).
                reduce(new Lines(0,0,0), (result,newO)-> result.sum(newO) );

        System.out.println(lines);
    }

    public static void ReadFromWithBufferedReaderConsumer(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        ConsumerOfLines consumer = new ConsumerOfLines();
        reader.lines().forEach(consumer);
        System.out.println(consumer);
    }
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Nikola Iliev\\IdeaProjects\\AP\\src\\AUD4\\WordCount.txt");
        ReadFromWithBufferedReaderBad(new FileInputStream(file));
        ReadFromWithBufferedReader(new FileInputStream(file));
        ReadFromWithBufferedReaderConsumer(new FileInputStream(file));
    }
}

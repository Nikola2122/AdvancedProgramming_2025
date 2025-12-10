package KOLOKVIUMSKI;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File2 implements Comparable<File2> {
    String name;
    int size;
    LocalDateTime createdAt;

    public File2(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isHidden(){
        return name.charAt(0) == '.';
    }
    public int getYear(){
        return createdAt.getYear();
    }

    @Override
    public int compareTo(File2 o) {
        return Comparator.comparing(File2::getCreatedAt).
                thenComparing(File2::getName).thenComparing(File2::getSize).compare(this, o);
    }

    public String getMonthDay(){
        return this.createdAt.getMonth().toString() + "-" + this.createdAt.getDayOfMonth();
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }
}

class FileSystem2{
    Map<Character, Set<File2>> map;

    public FileSystem2() {
        map = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        File2 f = new File2(name, size, createdAt);
        map.putIfAbsent(folder, new TreeSet<>());
        map.get(folder).add(f);
    }

    public List<File2> findAllHiddenFilesWithSizeLessThen(int size){
        return this.map.keySet().stream().flatMap(s -> map.get(s).stream()).
                filter(f -> f.isHidden() && f.getSize() < size).collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return map.keySet().stream().filter(folders::contains).
                flatMap(c -> map.get(c).stream()).mapToInt(File2::getSize).sum();
    }

    public Map<Integer, Set<File2>> byYear(){
        return map.keySet().stream().flatMap(s -> map.get(s).stream()).
                collect(Collectors.groupingBy(
                        File2::getYear,
                        TreeMap::new,
                        Collectors.toSet()
                ));
    }

    public Map<String, Long> sizeByMonthAndDay(){
        return map.keySet().stream().flatMap(s -> map.get(s).stream()).
                collect(Collectors.groupingBy(
                        File2::getMonthDay,
                        TreeMap::new,
                        Collectors.summingLong(File2::getSize)
                ));
    }
}

public class FileSystemTest2 {
    public static void main(String[] args) {
        FileSystem2 fileSystem = new FileSystem2();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File2> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File2>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File2> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here


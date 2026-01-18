package LABS.LAB5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


class Book{
    String isbn;
    String title;
    String author;
    int year;
    int copies;
    int totalBorrows;

    public Book(String isbn, String title, int year, String author) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.author = author;
        this.copies = 1;
        this.totalBorrows = 0;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getCopies() {
        return copies;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("%s - \"%s\" by %s (%d), available: %d, total borrows: %d", isbn, title, author, year, copies, totalBorrows);
    }
}

class Member{
    String id;
    String name;
    int borrowedBooks;
    int totalBorrowed;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = 0;
        this.totalBorrowed = 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - borrowed now: %d, total borrows: %d", name, id, borrowedBooks, totalBorrowed);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getTotalBorrowed() {
        return totalBorrowed;
    }
}

class LibrarySystem{
    String name;
    Map<String,Book> books;
    Map<String,List<String>> borrowers;
    Map<String,Queue<Member>> queue;
    Map<String,Member> members;

    public LibrarySystem(String name) {
        this.name = name;
        this.queue = new HashMap<>();
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.borrowers = new HashMap<>();
    }

    void registerMember(String id, String fullName){
        Member member = new Member(id, fullName);
        this.members.put(id, member);
    }

    void addBook(String isbn, String title, String author, int year){
        if(books.containsKey(isbn)){
            books.get(isbn).copies += 1;
        }
        else{
            books.put(isbn, new Book(isbn, title, year, author));
        }
    }

    void borrowBook(String memberId, String isbn){
        if(books.containsKey(isbn)){
            Book b = books.get(isbn);
            Member m = members.get(memberId);
            int copies = b.copies;
            if (copies>0){
                m.borrowedBooks++;
                m.totalBorrowed++;
                b.copies -= 1;
                b.totalBorrows++;
                borrowers.putIfAbsent(isbn, new ArrayList<>());
                borrowers.get(isbn).add(memberId);
            }
            else{
                queue.putIfAbsent(isbn, new LinkedList<>());
                queue.get(isbn).offer(m);
            }
        }
    }

    void returnBook(String memberId, String isbn){
        Member m = members.get(memberId);
        m.borrowedBooks--;
        Book b = books.get(isbn);
        b.copies++;
        borrowers.get(isbn).remove(memberId);
        if(queue.containsKey(isbn)){
            if(!queue.get(isbn).isEmpty()){
                Member next = queue.get(isbn).poll();
                borrowBook(next.id, isbn);
            }
        }
    }
    void printMembers(){
        this.members.values().stream().
                sorted(Comparator.comparing(Member::getBorrowedBooks, Comparator.reverseOrder()).thenComparing(Member::getName)).
                forEach(System.out::println);
    }

    void printBooks(){
        this.books.values().stream()
                .sorted(Comparator.comparing(Book::getTotalBorrows, Comparator.reverseOrder()).thenComparing(Book::getYear)).
                forEach(System.out::println);
    }

    void printBookCurrentBorrowers(String isbn){
        System.out.println(borrowers.get(isbn).stream().sorted().collect(Collectors.joining(", ")));
    }

    void printTopAuthors(){
        Map<String, Integer> map = books.values().stream().collect(Collectors.groupingBy(
                Book::getAuthor,
                HashMap::new,
                Collectors.collectingAndThen(
                        Collectors.summingInt(Book::getTotalBorrows),
                        Integer::intValue
                )
        ));
//                .entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()).
//                thenComparing(Map.Entry.comparingByKey())).forEach((e) -> System.out.println(e.getKey() + " - " + e.getValue()));
        map.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> v1,
                () -> new TreeMap<>(Comparator.comparing(map::get, Comparator.reverseOrder()))
        )).forEach((key, value) -> System.out.println(key + " - " + value));
    }

    Map<String, Book> getTopBookPerAuthor(){
        return books.values().stream().collect(Collectors.groupingBy(
                Book::getAuthor,
                TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparing(Book::getTotalBorrows)),
                        Optional::get
                )
        ));
    }

    Map <String, Integer> getBooksWaitingListSize(){
        return queue.entrySet().stream().
                sorted(Comparator.
                        comparing((Map.Entry<String,Queue<Member>> e) -> e.getValue().size(), Comparator.reverseOrder()).
                        thenComparing(Map.Entry::getKey))
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                (Map.Entry<String, Queue<Member>> e) -> e.getValue().size(),
                (v1,v2) -> v1,
                LinkedHashMap::new
        ));
    }

    Map <String, Integer> getBooksWaitingListSize2(){
        return queue.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        (Map.Entry<String, Queue<Member>> e) -> e.getValue().size(),
                        (v1,v2) -> v1,
                        () -> new TreeMap<>(Comparator.
                                comparing((String k)-> queue.get(k).size(), Comparator.reverseOrder()).
                                thenComparing(String::compareTo))
                ));
    }
}
public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

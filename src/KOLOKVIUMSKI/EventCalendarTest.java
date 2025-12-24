package KOLOKVIUMSKI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super(String.format("Wrong date: %s", message));
    }
}

class Event {
    private String name;
    private String location;
    private Date time;

    public Event(String name, String location, Date time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public Date getTime() { return time; }

    public LocalDate getLocalDate() {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalTime getLocalTime() {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm");
        LocalDateTime dt = LocalDateTime.of(getLocalDate(), getLocalTime());
        return dt.format(dtf) + " at " + location + ", " + name;
    }
}

class EventCalendar {
    private int year;
    private Map<LocalDate, Set<Event>> events;
    private int[] monthCount;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
        this.monthCount = new int[12]; // 0 = Jan, 11 = Dec
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        int eventYear = date.toInstant().atZone(ZoneId.systemDefault()).getYear();
        if (eventYear != this.year) {
            throw new WrongDateException(date.toString());
        }

        Event e = new Event(name, location, date);
        LocalDate dayKey = e.getLocalDate();

        // Create TreeSet with proper comparator if not exists
        events.putIfAbsent(dayKey, new TreeSet<>(
                Comparator.comparing(Event::getLocalTime)
                        .thenComparing(Event::getName)
        ));
        events.get(dayKey).add(e);

        // Increase month count
        int monthIndex = dayKey.getMonthValue() - 1; // 0-based
        monthCount[monthIndex]++;
    }

    public void listEvents(Date date) {
        LocalDate d = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Set<Event> eventsForDay = events.getOrDefault(d, Collections.emptySet());
        eventsForDay.forEach(System.out::println);
    }

    public void listByMonth() {
        TreeMap<Integer, Integer> tmpMap = events.entrySet().stream().collect(Collectors.groupingBy(
                (Map.Entry<LocalDate, Set<Event>> e) -> e.getKey().getMonth().getValue(),
                TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.counting(),
                        Long::intValue
                )
        ));
        IntStream.range(1, 13).forEach(i -> tmpMap.putIfAbsent(i, 0));

        tmpMap.forEach((k, v) -> System.out.println(k.toString() + " : " + v.toString()));
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }

        // Example: list events on a specific date
        Date queryDate = df.parse(scanner.nextLine());
        eventCalendar.listEvents(queryDate);

        // Example: list number of events per month
        eventCalendar.listByMonth();
    }
}

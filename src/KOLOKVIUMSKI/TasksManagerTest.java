package KOLOKVIUMSKI;


import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(String message) {
        super(String.format("The deadline %s has already passed", message));
    }
}

interface ITask {
    int priority();

    LocalDateTime deadline();

    String getCategory();
}

class Task1 implements ITask {
    String category;
    String name;
    String description;

    public Task1(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public LocalDateTime deadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

abstract class TaskDecorator implements ITask {
    ITask wrapped;

    public TaskDecorator(ITask wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getCategory() {
        return wrapped.getCategory();
    }
}

class deadlineDecorator extends TaskDecorator {
    LocalDateTime deadline;

    public deadlineDecorator(ITask wrapped, LocalDateTime deadline) {
        super(wrapped);
        this.deadline = deadline;
    }

    @Override
    public int priority() {
        return wrapped.priority();
    }

    @Override
    public LocalDateTime deadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return this.wrapped.toString().replace("}", "") +
                ", deadline=" + deadline + "}";
    }
}

class priorityDecorator extends TaskDecorator {
    int priority;

    public priorityDecorator(ITask wrapped, int priority) {
        super(wrapped);
        this.priority = priority;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public LocalDateTime deadline() {
        return wrapped.deadline();
    }

    @Override
    public String toString() {
        return this.wrapped.toString().replace("}", "") +
                ", priority=" + priority + "}";
    }
}

class TaskManager {
    Map<String, List<ITask>> tasks;


    static ITask create(String line) throws DeadlineNotValidException {
        String[] tokens = line.split(",");
        String category = tokens[0];
        String name = tokens[1];
        String description = tokens[2];

        if (tokens.length == 3) {
            return new Task1(category, name, description);
        } else if (tokens.length == 4) {
            if (tokens[3].contains("T")) {
                LocalDateTime deadline = LocalDateTime.parse(tokens[3]);
                if (deadline.isBefore(LocalDateTime.parse("2020-06-02T00:00:00"))) {
                    throw new DeadlineNotValidException(deadline.toString());
                }
                return new deadlineDecorator(new Task1(category, name, description), deadline);
            } else {
                return new priorityDecorator(new Task1(category, name, description), Integer.parseInt(tokens[3]));
            }
        } else {
            LocalDateTime deadline = LocalDateTime.parse(tokens[3]);
            if (deadline.isBefore(LocalDateTime.parse("2020-06-02T00:00:00"))) {
                throw new DeadlineNotValidException(deadline.toString());
            }
            return new priorityDecorator(
                    new deadlineDecorator(
                            new Task1(category, name, description), deadline
                    ), Integer.parseInt(tokens[4])
            );
        }
    }

    public void readTasks(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        tasks = reader.lines().map(l -> {
            try {
                return create(l);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(
                Collectors.groupingBy(
                        ITask::getCategory,
                        Collectors.toList()
                )
        );
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter writer = new PrintWriter(os);
        if (includeCategory) {
            if (includePriority) {
                tasks.forEach((k, v) -> {
                    writer.println(k.toUpperCase());
                    writer.println(v.stream().
                            sorted(Comparator.comparing(ITask::priority).
                                    thenComparing(ITask::deadline)
                                    )
                            .map(ITask::toString).collect(Collectors.joining("\n")));
                });
            } else {
                tasks.forEach((k, v) -> {
                    writer.println(k.toUpperCase());
                    writer.println(v.stream().
                            sorted(Comparator.comparing(ITask::deadline)
                            )
                            .map(ITask::toString).collect(Collectors.joining("\n")));
                });
            }
        } else {
            if (includePriority) {
                writer.println(tasks.values().stream().flatMap(List::stream).
                        sorted(Comparator.comparing(ITask::priority).
                                thenComparing(ITask::deadline)
                                )
                        .map(ITask::toString).collect(Collectors.joining("\n")));
            } else {
                writer.println(tasks.values().stream().flatMap(List::stream).
                        sorted(Comparator.
                                comparing(ITask::deadline)
                                )
                        .map(ITask::toString).collect(Collectors.joining("\n")));
            }
        }
        writer.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}

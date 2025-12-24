package AUD10_Design_Pattern;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class NumberHolderSingleton {
    int number;
    private static NumberHolderSingleton instance;

    private NumberHolderSingleton(int number) {
        this.number = number;
    }

    static synchronized NumberHolderSingleton getInstance() {
        if (instance == null){
            instance = new NumberHolderSingleton(0);
        }
        return instance;
    }

    synchronized void increment() {
        this.number++;
    }

    synchronized public int getNumber() {
        return number;
    }
}

class CallableImpl implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        NumberHolderSingleton.getInstance().increment();
        return NumberHolderSingleton.getInstance().getNumber();
    }
}

public class CallableSingleton {

    static void main() throws ExecutionException, InterruptedException {
        List<Callable<Integer>> list = IntStream.range(0, 100).mapToObj(i -> (new CallableImpl())).collect(Collectors.toList());
        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = list.stream().map(executor::submit).collect(Collectors.toList());
        for (Future<Integer> future : futures) {
            Integer result = future.get();
            System.out.println(result);
        }
        System.out.println("Done");
        System.out.println(NumberHolderSingleton.getInstance().getNumber());
        executor.shutdown();
    }
}

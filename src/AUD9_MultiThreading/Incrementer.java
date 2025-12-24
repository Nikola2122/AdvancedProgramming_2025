package AUD9_MultiThreading;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

class IncrementThread extends Thread {
    static int number = 0;
    static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        lock.lock();
        for (int i = 0; i < 10000; i++) {
            number++;
        }
        lock.unlock();
    }
}
public class Incrementer {
    static void main() throws InterruptedException {
        List<IncrementThread> threads = new ArrayList<>();

        IntStream.range(0, 10000).forEach(i -> {
            IncrementThread t = new IncrementThread();
            threads.add(t);
            t.start();
        });


        for (IncrementThread t : threads) {
            t.join();
        }

        System.out.println(IncrementThread.number);
    }
}

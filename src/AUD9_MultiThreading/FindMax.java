package AUD9_MultiThreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class FindMaxThread extends Thread {
    int start;
    int end;
    static ReentrantLock lock = new ReentrantLock();

    FindMaxThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            max = Math.max(max, FindMax.array[i]);
        }

        lock.lock();
        FindMax.MAX = Math.max(max, FindMax.MAX);
        lock.unlock();
    }
}

public class FindMax {
    static int arraySize = 10000;
    static int[] array = new int[arraySize];
    static int MAX = Integer.MIN_VALUE;
    static int chunkSize = 100;
    static Random random = new Random();

    static void main() {
        for (int i = 0; i < arraySize; i++) {
            array[i] = random.nextInt();
        }

        List<FindMaxThread> threads = new ArrayList<>();
        for (int i = 0; i < arraySize; i += chunkSize) {
            int start = i;
            int end = Math.min(i + chunkSize,  arraySize);

            FindMaxThread t = new FindMaxThread(start, end);
            threads.add(t);
            t.start();
        }

        for (FindMaxThread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(FindMax.MAX);

        System.out.println(Arrays.stream(array).max().orElse(0));

    }
}

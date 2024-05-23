package org.example;

import static java.lang.Thread.sleep;

public class ThreadRunningWays implements Runnable {

    static volatile int count = 0;

    public static void main(String[] args) throws InterruptedException {
        ThreadRunningWays threadRunningWays = new ThreadRunningWays();
        for (int i = 0; i < 10; i++) {
           Thread th = new Thread(threadRunningWays);
              th.start();
              th.join();
            Runnable runnable = () -> {
                System.out.println("Other Thread is running " + Thread.currentThread().getName());
                count++;
            };
            runnable.run();

        }

        System.out.println("Main thread is running and " + count + " threads are running in parallel");
    }

    @Override
    public void run() {
        System.out.println("Thread is running" + Thread.currentThread().getName());
        try {
            System.out.println("Thread is sleeping" + Thread.currentThread().getName());
            sleep(200);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println("Thread is running again" + Thread.currentThread().getName());
        count++;
    }
}

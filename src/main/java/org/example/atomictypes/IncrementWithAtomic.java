package org.example.atomictypes;

import java.util.concurrent.atomic.AtomicInteger;

public class IncrementWithAtomic {

    /**
     * We use AtomicInteger to make the count variable atomic.
     * uce volatile in this case is wrong because volatile only guarantees visibility and not atomicity.
     */
    //private volatile int count = 0;
    private AtomicInteger count = new AtomicInteger(0);

    private int nextInt() {
        return count.incrementAndGet();
    }

    public static void main(String[] args) {
        IncrementWithAtomic incrementWithAtomic = new IncrementWithAtomic();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementWithAtomic.nextInt();
                }
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final count is: " + incrementWithAtomic.count);
    }
}

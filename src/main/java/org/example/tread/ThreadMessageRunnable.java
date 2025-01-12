package org.example.tread;

public class ThreadMessageRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Am a runnable thread!" + Thread.currentThread().getName());
    }
}
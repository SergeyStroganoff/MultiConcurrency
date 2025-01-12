package org.example.tread;

public class ThreadMessage extends Thread {
    @Override
    public void run() {
        System.out.println("Am a thread!" + Thread.currentThread().getName());
    }
}

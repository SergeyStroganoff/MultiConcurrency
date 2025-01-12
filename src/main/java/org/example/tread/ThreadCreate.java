package org.example.tread;

import java.util.Arrays;

public class ThreadCreate {

    public static void main(String[] args) throws InterruptedException {
        Thread threadLocal = new Thread(() -> {
            System.out.println("Hello, World!");
        });
// Create a thread using
        //anonymous class
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Hello, Aninim World!");
            }
        };

        thread.start();

        // Create a thread using lambda
        new Thread(() -> System.out.println("Create and start thread using lambda!")).start();


        ThreadMessage threadMessage = new ThreadMessage();
        threadMessage.start();

        Thread threadRunnable = new Thread(new ThreadMessageRunnable());
        threadRunnable.start();

        threadLocal.start();
        System.out.println("getName:" + threadMessage.getName());
        System.out.println("THreadMessage is Alive:" + threadMessage.isAlive());
        System.out.println("THreadMessage getid:" + threadMessage.getId());
        System.out.println("THreadMessage priority:" + threadMessage.getPriority());
        System.out.println("THreadMessage getState:" + threadMessage.getState());
        System.out.println("THreadMessage group:" + threadMessage.getThreadGroup());
        System.out.println("THreadMessage trace:" + Arrays.toString(threadMessage.getStackTrace()));
        System.out.println("THreadMessage isDemon:" + threadMessage.isDaemon());
        System.out.println("THreadMessage isInterrupted:" + threadMessage.isInterrupted());

        // threadLocal.join();
        // threadMessage.join();

        System.out.println("Stop program");
    }
}


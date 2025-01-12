package org.example.interrupt;

public class LongThread implements Runnable {
    boolean isWorking = true;

    @Override
    public void run() {
        while (isWorking) {
            System.out.println("Long thread is running!");
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().isInterrupted());
                if (Thread.interrupted()) {
                    System.out.println("I am interrupted from job!");
                    System.out.println(Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt(); // после проверки статус флага автоматически переустанавливается на тру
                    System.out.println(Thread.currentThread().isInterrupted());
                    isWorking = false;
                    break;
                }
                System.out.println("I am doing my job now! " + i);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("I am interrupted from sleeping!");
                break;
            }
        }
    }
}

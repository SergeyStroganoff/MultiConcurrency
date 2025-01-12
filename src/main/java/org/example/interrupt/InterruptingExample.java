package org.example.interrupt;

public class InterruptingExample {
    public static void main(String[] args) throws InterruptedException {
        Thread longThread = new Thread(new LongThread());
        longThread.start();
        System.out.println("Interrupting the long thread!");
        longThread.interrupt();
        longThread.join();
        System.out.println("Main thread is done!");
    }
}

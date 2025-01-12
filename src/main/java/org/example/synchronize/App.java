package org.example.synchronize;

public class App {
    public static void main(String[] args) throws InterruptedException {
        final MonitorObject monitorObject = new MonitorObject();
        Thread waitThread = new Thread(new ThreadWait(monitorObject));
        waitThread.start();
        Thread.sleep(1000);
        new Thread(new NotifyThread(monitorObject)).start();
        waitThread.join();
        System.out.println("Main thread is done!");
        System.out.println("MonitorObject value: " + monitorObject.value);
    }
}

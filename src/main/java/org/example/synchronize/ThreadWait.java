package org.example.synchronize;

public class ThreadWait implements Runnable {

    private final MonitorObject monitorObject;

    public ThreadWait(MonitorObject monitorObject) {
        this.monitorObject = monitorObject;
    }

    @Override
    public void run() {
        System.out.println("ThreadWait is running");
        synchronized (monitorObject) {
            try {
                System.out.println("ThreadWait is working");
                monitorObject.value = 1;
                while (monitorObject.value == 1) { //проверка условия которого ждем
                    monitorObject.wait(); // иногда он глючит и выходит поэтому делаем while
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadWait is notified");
        }
    }
}

package org.example.synchronize;

public class NotifyThread implements Runnable {

    MonitorObject monitorObject;

    public NotifyThread(MonitorObject monitorObject) {
        this.monitorObject = monitorObject;
    }

    @Override
    public void run() {
        System.out.println("NotifyThread is running");
        synchronized (monitorObject) {
            System.out.println("NotifyThread is working");
            monitorObject.value = monitorObject.value-1;
            monitorObject.notify();
            System.out.println("NotifyThread is done");
        }
    }
}

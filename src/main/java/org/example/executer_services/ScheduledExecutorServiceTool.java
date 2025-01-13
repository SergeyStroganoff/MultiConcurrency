package org.example.executer_services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceTool {
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        scheduledExecutorService.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
        Thread.sleep(10000);
        scheduledExecutorService.shutdown();
    }
}

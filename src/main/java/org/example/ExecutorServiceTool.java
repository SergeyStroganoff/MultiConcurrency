package org.example;

import java.util.concurrent.*;

public class ExecutorServiceTool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> task = () -> Thread.currentThread().getName();
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            Future result = service.submit(task);
            System.out.println(result.get());
        }
        service.shutdown();
    }
}

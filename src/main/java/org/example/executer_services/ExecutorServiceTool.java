package org.example.executer_services;

import java.util.concurrent.*;

/*
    * The ExecutorServiceTool class demonstrates how to use the ExecutorService interface to create a thread pool
    * and submit tasks to it.
    * The ExecutorService interface provides a more flexible way to manage threads in a multithreaded application.
 */

public class ExecutorServiceTool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Create a Callable task that returns the name of the current thread.
        Callable<String> task = () -> Thread.currentThread().getName();
        // Create a thread pool with two threads.
        ExecutorService service = Executors.newFixedThreadPool(2);
        // Submit the task to the thread pool five times.
        for (int i = 0; i < 5; i++) {
            Future result = service.submit(task);
            System.out.println(result.get());
        }
        // Shut down the thread pool.
        service.shutdown();
    }
}

package org.example;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableAndFuture {
    public static void main(String[] args) throws Exception {
        Callable<String> task = () -> {
            return "Hello, World!";
        };
        FutureTask<String> future = new FutureTask<>(task);
        new Thread(future).start();
        System.out.println(future.get());
    }
}

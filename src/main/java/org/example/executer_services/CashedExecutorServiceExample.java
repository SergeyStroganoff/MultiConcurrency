package org.example.executer_services;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CashedExecutorServiceExample {

    private static final String start = "start";
    private static final String desireResult = "dert";
    private static final Random random = new Random();
    private static final int startLatinLawCaseSymbols = 97;
    private static final int endLatinLawCaseSymbols = 122;


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        String result = "";
        CashedExecutorServiceExample cashedExecutorServiceExample = new CashedExecutorServiceExample();
        ArrayList<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Future<String> stringFuture = executorService.submit(cashedExecutorServiceExample.new Task(start.length()));
            futures.add(stringFuture);
        }

        for (Future<String> future : futures) {
            try {
                String resultFromFuture = future.get();
                System.out.println("We get result:" + resultFromFuture);
                if (resultFromFuture.equals(desireResult)) {
                    result = resultFromFuture;
                    executorService.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Task implements Callable<String> {
        private final int length;
        private final String input = start;

        public Task(int length) {
            this.length = length;
        }

        @Override
        public String call() throws Exception {
            StringBuilder builder = new StringBuilder(length);
            while (!builder.toString().equals(input)) {
                for (int i = 0; i < length; i++) {
                    char newChar = (char) (startLatinLawCaseSymbols + random.nextInt(endLatinLawCaseSymbols - startLatinLawCaseSymbols + 1));
                    builder.append(newChar);
                }
            }
            return builder.toString();
        }
    }
}

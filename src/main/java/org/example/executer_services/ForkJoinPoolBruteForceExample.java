package org.example.executer_services;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolBruteForceExample {

    private static final String DESIRED_RESULT = "zbcde";
    private static final int WORD_LENGTH = DESIRED_RESULT.length();
    private static final int START_LATIN_LOWERCASE = 97;
    private static final int END_LATIN_LOWERCASE = 122;
    private static final int THRESHOLD = 1_000_000; // Количество комбинаций для одной задачи

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        long totalCombinations = (long) Math.pow(26, WORD_LENGTH);
        System.out.println("Total combinations to check: " + totalCombinations);
        long startTime = System.currentTimeMillis();
        String result = forkJoinPool.invoke(new BruteForceTask(0, totalCombinations));
        long endTime = System.currentTimeMillis();
        System.out.println("Password found: " + result);
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    static class BruteForceTask extends RecursiveTask<String> {
        private final long start;
        private final long end;

        public BruteForceTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected String compute() {
            // Если диапазон небольшой, выполняем поиск напрямую
            if ((end - start) <= THRESHOLD) {
                return bruteForceRangeCompute(start, end);
            }
            // Разделяем диапазон на две подзадачи
            long mid = start + (end - start) / 2;
            BruteForceTask leftTask = new BruteForceTask(start, mid);
            BruteForceTask rightTask = new BruteForceTask(mid, end);

            leftTask.fork(); // Выполняем левую задачу асинхронно
            String rightResult = rightTask.compute(); // Выполняем правую задачу
            String leftResult = leftTask.join(); // Ждем завершения левой задачи

            // Если один из результатов найден, возвращаем его
            if (!rightResult.isEmpty()) return rightResult;
            if (!leftResult.isEmpty()) return leftResult;
            return "";
        }

        private String bruteForceRangeCompute(long start, long end) {
                char[] word = new char[WORD_LENGTH];
            for (long i = start; i < end; i++) {
                long index = i;
                //print(index);
                for (int j = WORD_LENGTH - 1; j >= 0; j--) {
                    word[j] = (char) (START_LATIN_LOWERCASE + (index % 26));
                  //  print(word[j]);
                    index /= 26; // Т.е. путем деления получаем десятки и сотни - значение буквы 26 система исчесления
                }
                String attempt = new String(word);
                if (attempt.equals(DESIRED_RESULT)) {
                    return attempt;
                }
            }
            return "";
        }

        private void print(Object t) {
            System.out.println(t);
        }
    }
}


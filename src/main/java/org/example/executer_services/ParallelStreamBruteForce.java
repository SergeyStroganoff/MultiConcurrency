package org.example.executer_services;

import java.util.stream.IntStream;

public class ParallelStreamBruteForce {
    private static final int WORD_LENGTH = 5; // Длина слова
    private static final char START_LATIN_LOWERCASE = 'a'; // Начало диапазона символов
    private static final char END_LATIN_LOWERCASE = 'z'; // Конец диапазона символов
    private static final String DESIRED_RESULT = "zbcde"; // Искомое слово

    public static void main(String[] args) {
        // Общее количество возможных комбинаций
        long totalCombinations = (long) Math.pow(26, WORD_LENGTH);

        System.out.println("Starting brute force using parallel streams...");
        System.out.println("Total combinations to check: " + totalCombinations);
        // Используем параллельные стримы для перебора всех комбинаций
        long startTime = System.currentTimeMillis();
        String result = IntStream.range(0, (int) totalCombinations) // Диапазон индексов
                .parallel() // Включаем параллельный режим
                .mapToObj(ParallelStreamBruteForce::indexToWord) // Преобразуем индекс в слово
                .filter(word -> word.equals(DESIRED_RESULT)) // Фильтруем искомое слово
                .findFirst() // Находим первое совпадение
                .orElse(null); // Если ничего не найдено, возвращаем null

        long endTime = System.currentTimeMillis();

        if (result != null) {
            System.out.println("Found the word: " + result);
        } else {
            System.out.println("Word not found.");
        }
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    /**
     * Преобразует числовой индекс в слово.
     *
     * @param index Индекс в диапазоне [0, 26^WORD_LENGTH - 1]
     * @return Слово, соответствующее индексу
     */
    private static String indexToWord(int index) {
        char[] word = new char[WORD_LENGTH];
        for (int i = WORD_LENGTH - 1; i >= 0; i--) {
            word[i] = (char) (START_LATIN_LOWERCASE + (index % 26)); // Вычисляем символ
            index /= 26; // Переходим к следующей позиции
        }
        return new String(word);
    }
}


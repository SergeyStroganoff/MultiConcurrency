package org.example.concurrent_collections;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Объяснение:
 * ConcurrentLinkedQueue:
 * <p>
 * Это неблокирующая очередь, которая позволяет безопасно работать с элементами в многопоточной среде.
 * Метод add() добавляет элемент в конец очереди.
 * Метод poll() извлекает и удаляет элемент из начала очереди или возвращает null, если очередь пуста.
 * Производители (Producers):
 * <p>
 * Проверяют, не превышает ли размер очереди максимальную вместимость.
 * Если очередь не полна, добавляют новый элемент и выводят сообщение в консоль.
 * Если очередь заполнена, производитель ожидает.
 * Потребители (Consumers):
 * <p>
 * Извлекают элементы из очереди с помощью poll().
 * Если очередь пуста, выводят сообщение о том, что нужно ждать.
 * Использование ExecutorService:
 * <p>
 * Упрощает управление потоками. В данном случае используется фиксированный пул потоков с 3 потоками.
 * Ограничение очереди:
 * <p>
 * Очередь сама по себе не ограничена, поэтому максимальная вместимость (maxCapacity) проверяется вручную.
 */

public class ProducerConsumerConcurrentLinkedQueue {
    public static void main(String[] args) {
        // Общая очередь для Producer и Consumer
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        int maxCapacity = 10;

        // Создаём пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Запускаем 2 производителя
        executor.execute(new Producer(queue, maxCapacity));
        executor.execute(new Producer(queue, maxCapacity));

        // Запускаем 1 потребителя
        executor.execute(new Consumer(queue));

        // Завершаем работу пула потоков через 10 секунд
        executor.shutdown();
    }
}

class Producer implements Runnable {
    private final ConcurrentLinkedQueue<Integer> queue;
    private final int maxCapacity;
    private static int value = 0;

    public Producer(ConcurrentLinkedQueue<Integer> queue, int maxCapacity) {
        this.queue = queue;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (queue.size() < maxCapacity) {
                    int producedValue = ++value;
                    queue.add(producedValue);
                    System.out.println(Thread.currentThread().getName() + " produced: " + producedValue);
                } else {
                    System.out.println(Thread.currentThread().getName() + " queue is full, waiting...");
                }
                Thread.sleep(500); // Симуляция задержки
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final ConcurrentLinkedQueue<Integer> queue;

    public Consumer(ConcurrentLinkedQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer consumedValue = queue.poll();
                if (consumedValue != null) {
                    System.out.println(Thread.currentThread().getName() + " consumed: " + consumedValue);
                } else {
                    System.out.println(Thread.currentThread().getName() + " queue is empty, waiting...");
                }
                Thread.sleep(1000); // Симуляция задержки
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


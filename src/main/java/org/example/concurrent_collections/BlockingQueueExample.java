package org.example.concurrent_collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Различия между ConcurrentLinkedQueue и BlockingQueue
 * Характеристика	ConcurrentLinkedQueue	BlockingQueue
 * Тип очереди	Неблокирующая (non-blocking) очередь.	Блокирующая очередь.
 * Блокировка потоков:
 * - Потоки не блокируются, если очередь пуста или полна. Методы возвращают null или выбрасывают исключение.
 * - Потоки блокируются, если очередь пуста (при чтении) или полна (при записи).
 * Поддержка лимитов:
 * - Не поддерживает ограничение размера очереди (не имеет встроенной ёмкости).
 * - Поддерживает фиксированную ёмкость. Потоки ждут, пока не освободится место/элемент.
 * Методы ожидания:
 * -Не предоставляет методов ожидания (poll, offer возвращают null или false).
 * -Методы put и take блокируют потоки до выполнения операции.
 * Скорость:
 * - Высокая скорость за счёт неблокирующей архитектуры (использует CAS-операции).
 * - Потенциально медленнее, так как поток блокируется в ожидании места или элемента.
 * Использование:
 * - Используется в случаях, где операции ожидания не нужны (например, для пулов задач).
 * - Используется там, где важно синхронизировать потоки при доступе к элементам очереди.
 * Методы синхронизации:
 * - Методы основаны на оптимистичной блокировке (CAS - Compare-And-Swap).
 * - Методы основаны на Lock и Condition для реализации блокирующего поведения.
 */

public class BlockingQueueExample {

    public static void main(String[] args) {
        // Общая очередь с максимальной ёмкостью 10
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        // Создаём пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // Запускаем 2 производителя
        executor.execute(new BlockProducer(queue));
        executor.execute(new BlockProducer(queue));
        // Запускаем 1 потребителя
        executor.execute(new BlockConsumer(queue));
        // Завершаем работу пула потоков через 10 секунд
        executor.shutdown();
    }
}

class BlockProducer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private static int value = 0;

    public BlockProducer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int producedValue = ++value;
                queue.put(producedValue); // Блокируется, если очередь полна
                System.out.println(Thread.currentThread().getName() + " produced: " + producedValue);
                Thread.sleep(500); // Симуляция задержки
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class BlockConsumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    public BlockConsumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int consumedValue = queue.take(); // Блокируется, если очередь пуста
                System.out.println(Thread.currentThread().getName() + " consumed: " + consumedValue);
                Thread.sleep(1000); // Симуляция задержки
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


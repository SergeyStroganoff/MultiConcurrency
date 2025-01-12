package org.example.sync_tools;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock - это реализация интерфейса Lock, которая используется для синхронизации потоков.
 * ReentrantLock позволяет потокам заблокировать и разблокировать монитор.
 * ReentrantLock может быть использован вместо synchronized блока.
 * Condition - это механизм, который позволяет потокам ожидать определенного условия.
 */

public class ReentrantLockTool {


    private static int count = 0;
    private final Lock lock = new ReentrantLock();
    // private final Lock lock = new ReentrantLock(true); обеспечиваем справедливость выстраивания потоков в очереди
    Condition printCondition = lock.newCondition();
    Condition processCondition = lock.newCondition();
    // инструмент для механизма await/signal (wait/notify)
    boolean isLoggerWorking = true;

    public static void main(String[] args) {

        ReentrantLockTool reentrantLockTool = new ReentrantLockTool();
        Counter counter = reentrantLockTool.new Counter();
        ProcessLogger processLogger = reentrantLockTool.new ProcessLogger();
        Thread logger = new Thread(processLogger);
        // logger.setDaemon(true);
        logger.start();

        for (int i = 0; i < 10; i++) {
            new Thread(counter).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reentrantLockTool.isLoggerWorking = false;
        System.out.println("Final count is: " + count);
    }

    class ProcessLogger implements Runnable {
        @Override
        public void run() {
            while (isLoggerWorking) {
                try {
                    lock.lock();
                    printCondition.await(); // Ждем сигнала от Counter
                    System.out.println("Log: Counter is: " + count);
                    processCondition.signal(); // сигнал для другого потока
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Counter implements Runnable {
        public void increment() {
            lock.lock();
            try {

                for (int i = 0; i < 100; i++) {
                    count++;
                    if (count % 10 == 0) {
                        System.out.println("count is: " + count);
                        printCondition.signal(); // сигнал для другого потока
                        processCondition.await(); // поток ожидает сигнала от другого потока если число делится на 10
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void run() {
            increment();
        }
    }

}

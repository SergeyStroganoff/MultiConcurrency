package org.example.sync_tools;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock - это реализация интерфейса ReadWriteLock, которая используется для синхронизации потоков.
 * ReentrantReadWriteLock позволяет потокам заблокировать и разблокировать монитор.
 * При использовании ReentrantReadWriteLock, потоки могут блокировать монитор для чтения или записи.
 * Потоки, которые блокируют монитор для чтения, могут читать данные, но не могут их изменять.
 * Потоки, которые блокируют монитор для записи, могут изменять данные.
 * ReentrantReadWriteLock может быть использован вместо synchronized блока.
 */

public class ReentantReadWriteLockExample {

    public static void main(String[] args) {
        SharedData sharedData = new SharedData();
        // Create reader threads
        for (int i = 1; i <= 3; i++) {
            new Thread(new Reader(sharedData), "Reader-" + i).start();
        }

        // Create writer threads
        for (int i = 1; i <= 2; i++) {
            new Thread(new Writer(sharedData), "Writer-" + i).start();
        }
    }
}

class SharedData {
    private String data = "Initial Data";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void readData() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is reading data: " + data);
            Thread.sleep(1000); // Simulate read delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " finished reading.");
            lock.readLock().unlock();
        }
    }

    public void writeData(String newData) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is writing data: " + newData);
            Thread.sleep(1500); // Simulate write delay
            this.data = newData;
            System.out.println(Thread.currentThread().getName() + " finished writing.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

class Reader implements Runnable {
    private final SharedData sharedData;

    public Reader(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        while (true) {
            sharedData.readData();
            try {
                Thread.sleep(500); // Delay between reads
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Writer implements Runnable {
    private final SharedData sharedData;

    public Writer(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        int counter = 1;
        while (true) {
            sharedData.writeData("New Data " + counter++);
            try {
                Thread.sleep(2000); // Delay between writes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

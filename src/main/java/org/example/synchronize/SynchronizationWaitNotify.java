package org.example.synchronize;

public class SynchronizationWaitNotify {

    private volatile int count;

    public static void main(String[] args) {
        int[] buffer = new int[10];
        SynchronizationWaitNotify synchronizationWaitNotify = new SynchronizationWaitNotify();
        Producer producer = synchronizationWaitNotify.new Producer(buffer);
        Consumer consumer = synchronizationWaitNotify.new Consumer(buffer);
        producer.start();
        consumer.start();
    }


    class Producer extends Thread {
        private final int[] buffer;

        public Producer(int[] buffer) {
            this.buffer = buffer;
        }

        public void produce() {
            synchronized (buffer) {
                if (count == buffer.length-1) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Produce " + count);
                buffer[count++] = 1;
                buffer.notify();
            }
        }

        @Override
        public void run() {
            while (true) {
                produce();
            }
        }
    }

    class Consumer extends Thread {
        private final int[] buffer;

        public Consumer(int[] buffer) {
            this.buffer = buffer;
        }

        public void consume() throws InterruptedException {
            synchronized (buffer) {
                if (count == 0) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer[count--] = 0;
                System.out.println("Consume " + count);
                this.sleep(500);
                buffer.notifyAll();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}

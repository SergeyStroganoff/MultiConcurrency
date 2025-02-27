package org.example.sync_tools;

import java.util.concurrent.Semaphore;
/**
 * Синхронизатор Semaphore реализует шаблон синхронизации Семафор.
 * Чаще всего, семафоры необходимы, когда нужно ограничить доступ к некоторому общему ресурсу.
 * В конструктор этого класса (Semaphore(int permits) или Semaphore(int permits, boolean fair))
 * обязательно передается количество потоков, которому семафор будет разрешать одновременно использовать
 * заданный ресурс.
 * Затем, чтобы получить доступ к ресурсу, поток должен вызвать метод acquire(),
 * чтобы освободить ресурс - release().
 */

public class SemaphoreTool {
    //Парковочное место занято - true, свободно - false
    private static final boolean[] PARKING_PLACES = new boolean[5];
    //Устанавливаем флаг "справедливый", в таком случае метод
    //aсquire() будет раздавать разрешения в порядке очереди
    private static final Semaphore SEMAPHORE = new Semaphore(5, true);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            new Thread(new Car(i)).start();
            Thread.sleep(400);
        }
    }

    public static class Car implements Runnable {
        private final int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        public void run() {
            System.out.printf("Автомобиль №%d подъехал к парковке.\n", carNumber);
            try {
                //acquire() запрашивает доступ к следующему за вызовом этого метода блоку кода,
                //если доступ не разрешен, поток вызвавший этот метод блокируется до тех пор,
                //пока семафор не разрешит доступ
                SEMAPHORE.acquire();

                int parkingNumber = -1;

                //Ищем свободное место и паркуемся
                synchronized (PARKING_PLACES){
                    for (int i = 0; i < 5; i++)
                        if (!PARKING_PLACES[i]) {      //Если место свободно
                            PARKING_PLACES[i] = true;  //занимаем его
                            parkingNumber = i;         //Наличие свободного места, гарантирует семафор
                            System.out.printf("Автомобиль №%d припарковался на месте %d.\n", carNumber, i);
                            break;
                        }
                }
                Thread.sleep(5000);       //Уходим за покупками, к примеру
                // делаем что-то еще на синхронизированных данных

                synchronized (PARKING_PLACES) {
                    PARKING_PLACES[parkingNumber] = false;//Освобождаем место
                }

                System.out.printf("Автомобиль №%d покинул парковку.\n", carNumber);
            } catch (InterruptedException e) {
            } finally { //В конце работы парковки освобождаем разрешение в любом случае
                //release(), напротив, освобождает ресурс
                SEMAPHORE.release();
            }
        }
    }
}

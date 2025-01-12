package org.example;

import java.util.concurrent.CountDownLatch;

/**
 * Синхронизатор CountDownLatch реализует шаблон синхронизации Замок с обратным отсчетом.
 * Можно использовать только один раз, затем надо создавать новый объект.
 * CountDownLatch (замок с обратным отсчетом) предоставляет возможность любому количеству потоков в блоке кода
 * ожидать до тех пор, пока не завершится определенное количество операций,
 * выполняющихся в других потоках, перед тем как они будут «отпущены»,
 * чтобы продолжить свою деятельность. т.е. накопление и Лавина
 * Другими словами если у потоков есть какой то блок инициализации разных ресурсов и разный по размеру то мы можем обеспечить
 * что все потоки будут готовы к работе одновременно. Т.е. подойдут и начнут выполнять работу одновремнно.
 * <p>
 * В конструктор CountDownLatch (CountDownLatch(int count)) обязательно передается количество операций,
 * которое должно быть выполнено, чтобы замок «отпустил» заблокированные потоки.
 * <p>
 * Рассмотрим следующий пример. Мы хотим провести автомобильную гонку.
 * В гонке принимают участие пять автомобилей. Для начала гонки нужно, чтобы выполнились следующие условия:
 * Каждый из пяти автомобилей подъехал к стартовой прямой
 * Была дана команда «На старт!»;
 * Была дана команда «Внимание!»;
 * Была дана команда «Марш!».
 * Важно, чтобы все автомобили стартовали одновременно.
 */

public class CountDownLatchTool {

    //Создаем CountDownLatch на 8 "условий"
    private static final CountDownLatch START = new CountDownLatch(8);
    //Условная длина гоночной трассы
    private static final int trackLength = 500000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }

        while (START.getCount() > 3) {//Проверяем, собрались ли все автомобили
            Thread.sleep(100);
        }          //у стартовой прямой. Если нет, ждем 100ms

        Thread.sleep(1000);
        System.out.println("На старт!");
        START.countDown();//Команда дана, уменьшаем счетчик на 1
        Thread.sleep(1000);
        System.out.println("Внимание!");
        START.countDown();//Команда дана, уменьшаем счетчик на 1
        Thread.sleep(1000);
        System.out.println("Марш!");
        START.countDown();//Команда дана, уменьшаем счетчик на 1
        //счетчик становится равным нулю, и все ожидающие потоки
        //одновременно разблокируются
    }

    public static class Car implements Runnable {
        private final int carNumber;
        private final int carSpeed;//считаем, что скорость автомобиля постоянная

        public Car(int carNumber, int carSpeed) {
            this.carNumber = carNumber;
            this.carSpeed = carSpeed;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к стартовой прямой.\n", carNumber);
                //Автомобиль подъехал к стартовой прямой - условие выполнено
                //уменьшаем счетчик на 1
                START.countDown();
                //метод await() блокирует поток, вызвавший его, до тех пор, пока
                //счетчик CountDownLatch не станет равен 0 - т.е. пока все условия не будут выполнены
                START.await();
                Thread.sleep(trackLength / carSpeed);//ждем пока проедет трассу
                System.out.printf("Автомобиль №%d финишировал!\n", carNumber);
            } catch (InterruptedException e) {
            }
        }
    }
}


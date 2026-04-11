package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demostración de ArrayBlockingQueue con patrón Productor/Consumidor.
 *
 * <p>Muestra el patrón clásico de productor/consumidor con back-pressure
 * cuando el buffer está lleno. Incluye demostración de timeout y manejo
 * de cola llena/vacía.
 */
public final class ArrayBlockingQueueDemo implements Demo {

    private static final int QUEUE_CAPACITY = 5;
    private static final int TOTAL_ITEMS = 15;
    private static final int PRODUCER_DELAY_MS = 50;
    private static final int CONSUMER_DELAY_MS = 100;

    @Override
    public String id() {
        return "array-blocking-queue";
    }

    @Override
    public String title() {
        return "ArrayBlockingQueue - Producer/Consumer (java.util.concurrent)";
    }

    @Override
    public String learningObjective() {
        return "Implementar el patrón productor/consumidor con back-pressure y timeout.";
    }

    @Override
    public String expectedObservation() {
        return "El productor espera cuando el buffer está lleno; el consumidor espera cuando está vacío.";
    }

    @Override
    public String keyTakeaway() {
        return "BlockingQueue es ideal para flujos controlados con back-pressure entre threads.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        showConstructors(result);
        demonstrateProducerConsumer(result);
        demonstrateBackPressure(result);
        demonstrateTimeoutHandling(result);

        return result.build();
    }

    private void showConstructors(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "CONSTRUCTORES");

        ArrayBlockingQueue<String> defaultQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        result.add(MessageType.DEBUG, "Capacidad " + QUEUE_CAPACITY + ": " + defaultQueue);

        ArrayBlockingQueue<String> fairQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY, true);
        result.add(MessageType.DEBUG, "Fair ordering: " + fairQueue);

        java.util.Collection<String> initial = java.util.List.of("A", "B", "C");
        ArrayBlockingQueue<String> withElements = new ArrayBlockingQueue<>(QUEUE_CAPACITY, false, initial);
        result.add(MessageType.DEBUG, "Con elementos iniciales: " + withElements);
        result.add(MessageType.MESSAGE, "Capacidad restante: " + (QUEUE_CAPACITY - withElements.size()));
    }

    private void demonstrateProducerConsumer(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "PATRÓN PRODUCTOR/CONSUMIDOR");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);

        result.add(MessageType.INFO, "Cola con capacidad: " + QUEUE_CAPACITY);
        result.add(MessageType.INFO, "Total de items a producir: " + TOTAL_ITEMS);
        result.add(MessageType.INFO, "Productor más rápido que consumidor (" +
                PRODUCER_DELAY_MS + "ms vs " + CONSUMER_DELAY_MS + "ms)");

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= TOTAL_ITEMS; i++) {
                try {
                    String item = "Item-" + i;
                    queue.put(item);
                    produced.incrementAndGet();
                    Thread.sleep(PRODUCER_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            while (consumed.get() < TOTAL_ITEMS) {
                try {
                    String item = queue.take();
                    consumed.incrementAndGet();
                    Thread.sleep(CONSUMER_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.add(MessageType.SUCCESS, "Producidos: " + produced.get() + ", Consumidos: " + consumed.get());
    }

    private void demonstrateBackPressure(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "BACK-PRESSURE (PRESIÓN DE RETORNO)");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        AtomicInteger blockedCount = new AtomicInteger(0);

        result.add(MessageType.INFO, "Cola pequeña (capacidad=3), productor rápido:");

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    long start = System.currentTimeMillis();
                    queue.put("Item-" + i);
                    long elapsed = System.currentTimeMillis() - start;

                    if (elapsed > 10) {
                        blockedCount.incrementAndGet();
                        result.add(MessageType.DEBUG, "  Item-" + i + " bloqueó " + elapsed + "ms (cola llena)");
                    } else {
                        result.add(MessageType.DEBUG, "  Item-" + i + " agregado inmediatamente");
                    }

                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "FastProducer");

        Thread slowConsumer = new Thread(() -> {
            try {
                Thread.sleep(100);
                for (int i = 0; i < 8; i++) {
                    queue.take();
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "SlowConsumer");

        slowConsumer.start();
        producer.start();

        try {
            producer.join();
            slowConsumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.add(MessageType.MESSAGE, "El productor fue bloqueado " + blockedCount.get() +
                " veces esperando espacio en la cola.");
        result.add(MessageType.MESSAGE, "Esto es back-pressure: el productor se ralentiza automáticamente.");
    }

    private void demonstrateTimeoutHandling(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "TIMEOUT Y MANEJO DE COLA LLENA/VACÍA");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

        result.add(MessageType.INFO, "offer() con timeout cuando la cola está llena:");
        queue.offer("A");
        queue.offer("B");
        result.add(MessageType.DEBUG, "Cola llena: " + queue);

        try {
            long start = System.currentTimeMillis();
            boolean added = queue.offer("C", 200, TimeUnit.MILLISECONDS);
            long elapsed = System.currentTimeMillis() - start;

            if (!added) {
                result.add(MessageType.MESSAGE, "offer() retornó false después de " + elapsed + "ms (timeout)");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.add(MessageType.INFO, "poll() con timeout cuando la cola está vacía:");
        BlockingQueue<String> emptyQueue = new ArrayBlockingQueue<>(2);

        try {
            long start = System.currentTimeMillis();
            String item = emptyQueue.poll(300, TimeUnit.MILLISECONDS);
            long elapsed = System.currentTimeMillis() - start;

            if (item == null) {
                result.add(MessageType.MESSAGE, "poll() retornó null después de " + elapsed + "ms (timeout)");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.add(MessageType.INFO, "drainTo() para vaciar rápidamente:");
        queue.offer("X");
        queue.offer("Y");
        java.util.List<String> drained = new java.util.ArrayList<>();
        queue.drainTo(drained);
        result.add(MessageType.SUCCESS, "Elementos drenados: " + drained);

        result.add(MessageType.MESSAGE, "Métodos no-bloqueantes (add/remove/element) lanzan excepciones si fallan.");
        result.add(MessageType.MESSAGE, "Métodos bloqueantes (put/take) esperan indefinidamente.");
        result.add(MessageType.MESSAGE, "Métodos con timeout (offer/poll) esperan un tiempo máximo.");
    }
}

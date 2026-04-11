package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.concurrency.CompletionStatus;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demostración de ConcurrentLinkedQueue.
 *
 * <p>Muestra el comportamiento no-bloqueante de una cola concurrente
 * comparada con LinkedList tradicional. ConcurrentLinkedQueue usa
 * algoritmos lock-free para operaciones thread-safe sin bloqueos explícitos.
 */
public final class ConcurrentLinkedQueueDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;
    private static final int THREAD_COUNT = 5;
    private static final int ITEMS_PER_THREAD = 100;

    @Override
    public String id() {
        return "concurrent-linked-queue";
    }

    @Override
    public String title() {
        return "ConcurrentLinkedQueue (java.util.concurrent)";
    }

    @Override
    public String learningObjective() {
        return "Comparar LinkedList (no thread-safe) vs ConcurrentLinkedQueue (lock-free).";
    }

    @Override
    public String expectedObservation() {
        return "LinkedList pierde elementos o falla; ConcurrentLinkedQueue procesa todos sin bloqueo.";
    }

    @Override
    public String keyTakeaway() {
        return "ConcurrentLinkedQueue es ideal para colas FIFO de alta concurrencia sin bloqueos.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        showConstructors(result);
        demonstrateConcurrentAdditions(result);
        demonstrateNoConcurrentModificationException(result);
        demonstrateNonBlockingBehavior(result);

        return result.build();
    }

    private void showConstructors(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "CONSTRUCTORES");

        ConcurrentLinkedQueue<String> emptyQueue = new ConcurrentLinkedQueue<>();
        emptyQueue.offer("Primero");
        emptyQueue.offer("Segundo");
        result.add(MessageType.DEBUG, "Vacío: " + emptyQueue);

        java.util.Collection<String> source = java.util.List.of("A", "B", "C");
        ConcurrentLinkedQueue<String> fromCollection = new ConcurrentLinkedQueue<>(source);
        result.add(MessageType.DEBUG, "Desde colección: " + fromCollection);
    }

    private void demonstrateConcurrentAdditions(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "INSERCIONES CONCURRENTES");

        result.add(MessageType.INFO, "LinkedList (no thread-safe) con " + THREAD_COUNT + " threads:");
        Queue<String> linkedList = new LinkedList<>();
        AtomicInteger linkedListCount = runConcurrentAdditions(linkedList);
        result.add(MessageType.LOGIC_ERROR, "Elementos esperados: " + (THREAD_COUNT * ITEMS_PER_THREAD) +
                ", reales: " + linkedListCount.get());

        result.add(MessageType.INFO, "ConcurrentLinkedQueue (lock-free) con " + THREAD_COUNT + " threads:");
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        AtomicInteger concurrentCount = runConcurrentAdditions(concurrentQueue);
        result.add(MessageType.SUCCESS, "Elementos esperados: " + (THREAD_COUNT * ITEMS_PER_THREAD) +
                ", reales: " + concurrentCount.get());
    }

    private AtomicInteger runConcurrentAdditions(Queue<String> queue) {
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(THREAD_COUNT);

        try {
            for (int t = 0; t < THREAD_COUNT; t++) {
                final int threadId = t;
                executor.execute(() -> {
                    for (int i = 0; i < ITEMS_PER_THREAD; i++) {
                        if (queue.offer("Thread-" + threadId + "-Item-" + i)) {
                            counter.incrementAndGet();
                        }
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        try {
            executor.awaitTermination(EXECUTOR_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return counter;
    }

    private void demonstrateNoConcurrentModificationException(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "SIN CONCURRENTMODIFICATIONEXCEPTION");

        result.add(MessageType.INFO, "Iterando LinkedList mientras se agregan elementos:");
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("A");
        linkedList.add("B");

        ExecutorService executor = AsyncTaskSupport.startSingleTask(() -> {
            try {
                Thread.sleep(50);
                linkedList.addLast("C");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        boolean exceptionThrown = false;
        try {
            for (String item : linkedList) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            exceptionThrown = true;
            result.add(MessageType.EXCEPTION, "ConcurrentModificationException lanzada");
        }

        if (!exceptionThrown) {
            result.add(MessageType.MESSAGE, "Sin excepción (depende del timing)");
        }

        CompletionStatus status = AsyncTaskSupport.awaitCompletion(executor, EXECUTOR_TIMEOUT_SECONDS);
        if (status == CompletionStatus.TIMED_OUT) {
            result.add(MessageType.ERROR, "Timeout esperando executor");
        }

        result.add(MessageType.INFO, "Iterando ConcurrentLinkedQueue mientras se agregan elementos:");
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        concurrentQueue.offer("A");
        concurrentQueue.offer("B");

        ExecutorService executor2 = AsyncTaskSupport.startSingleTask(() -> {
            try {
                Thread.sleep(50);
                concurrentQueue.offer("C");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        int count = 0;
        try {
            for (String item : concurrentQueue) {
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            result.add(MessageType.EXCEPTION, "ConcurrentModificationException (inesperada)");
        }

        result.add(MessageType.SUCCESS, "Iteración completada sin excepción, elementos vistos: " + count);

        CompletionStatus status2 = AsyncTaskSupport.awaitCompletion(executor2, EXECUTOR_TIMEOUT_SECONDS);
        if (status2 == CompletionStatus.TIMED_OUT) {
            result.add(MessageType.ERROR, "Timeout esperando executor");
        }
    }

    private void demonstrateNonBlockingBehavior(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "COMPORTAMIENTO NO-BLOQUEANTE");

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        result.add(MessageType.INFO, "poll() en cola vacía retorna null inmediatamente:");
        long startTime = System.currentTimeMillis();
        String item = queue.poll();
        long elapsed = System.currentTimeMillis() - startTime;
        result.add(MessageType.DEBUG, "Resultado: " + item + ", tiempo: " + elapsed + "ms");

        result.add(MessageType.INFO, "offer() nunca bloquea al productor:");
        queue.offer("Item1");
        queue.offer("Item2");
        result.add(MessageType.DEBUG, "Cola después de offers: " + queue);

        result.add(MessageType.MESSAGE, "Nota: ConcurrentLinkedQueue no bloquea, " +
                "pero también no espera si está llena/vacía.");
        result.add(MessageType.MESSAGE, "Para bloqueo con back-pressure, usar ArrayBlockingQueue.");
    }
}

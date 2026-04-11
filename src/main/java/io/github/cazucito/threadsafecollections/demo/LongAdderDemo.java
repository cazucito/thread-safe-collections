package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Demostración de LongAdder para contadores concurrentes.
 *
 * <p>Compara AtomicLong vs LongAdder bajo alta contención y demuestra
 * el uso de ConcurrentHashMap con LongAdder para contadores de frecuencia.
 * LongAdder es más escalable que AtomicLong cuando muchos threads
 * actualizan concurrentemente.
 */
public final class LongAdderDemo implements Demo {

    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 100_000;
    private static final int TIMEOUT_SECONDS = 10;

    @Override
    public String id() {
        return "long-adder";
    }

    @Override
    public String title() {
        return "LongAdder - Contadores Concurrentes (java.util.concurrent.atomic)";
    }

    @Override
    public String learningObjective() {
        return "Comparar AtomicLong vs LongAdder y usar ConcurrentHashMap + LongAdder para frecuencias.";
    }

    @Override
    public String expectedObservation() {
        return "LongAdder tiene mejor throughput que AtomicLong bajo alta contención.";
    }

    @Override
    public String keyTakeaway() {
        return "Usar LongAdder para contadores frecuentemente actualizados; AtomicLong para valores que se leen mucho.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        showLongAdderBasics(result);
        demonstrateAtomicLongVsLongAdder(result);
        demonstrateFrequencyCounter(result);
        explainWhenToUse(result);

        return result.build();
    }

    private void showLongAdderBasics(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "LONGADDER BÁSICO");

        LongAdder counter = new LongAdder();
        result.add(MessageType.DEBUG, "Inicial: " + counter.sum());

        counter.increment();
        counter.increment();
        counter.add(5);
        result.add(MessageType.DEBUG, "Después de increment()+increment()+add(5): " + counter.sum());

        counter.decrement();
        result.add(MessageType.DEBUG, "Después de decrement(): " + counter.sum());

        long sumThenReset = counter.sumThenReset();
        result.add(MessageType.DEBUG, "sumThenReset() retornó: " + sumThenReset);
        result.add(MessageType.DEBUG, "Valor después de reset: " + counter.sum());

        result.add(MessageType.MESSAGE, "LongAdder usa stripe de contadores internos para reducir contención.");
    }

    private void demonstrateAtomicLongVsLongAdder(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ATOMICLONG VS LONGADDER - RENDIMIENTO");

        result.add(MessageType.INFO, "Threads: " + THREAD_COUNT);
        result.add(MessageType.INFO, "Incrementos por thread: " + INCREMENTS_PER_THREAD);
        result.add(MessageType.INFO, "Total de operaciones: " + (THREAD_COUNT * INCREMENTS_PER_THREAD));

        AtomicLong atomicLong = new AtomicLong(0);
        long atomicTime = runCounterTest(atomicLong::incrementAndGet);
        result.add(MessageType.MESSAGE, "AtomicLong: " + atomicTime + "ms");

        LongAdder longAdder = new LongAdder();
        long adderTime = runCounterTest(longAdder::increment);
        result.add(MessageType.MESSAGE, "LongAdder: " + adderTime + "ms");

        if (adderTime < atomicTime) {
            double improvement = ((double) (atomicTime - adderTime) / atomicTime) * 100;
            result.add(MessageType.SUCCESS, String.format("LongAdder fue %.1f%% más rápido", improvement));
        }

        result.add(MessageType.DEBUG, "AtomicLong final: " + atomicLong.get());
        result.add(MessageType.DEBUG, "LongAdder final: " + longAdder.sum());
    }

    private long runCounterTest(Runnable incrementOperation) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        long startTime = System.currentTimeMillis();

        try {
            for (int t = 0; t < THREAD_COUNT; t++) {
                executor.execute(() -> {
                    for (int i = 0; i < INCREMENTS_PER_THREAD; i++) {
                        incrementOperation.run();
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        try {
            executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return System.currentTimeMillis() - startTime;
    }

    private void demonstrateFrequencyCounter(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "CONCURRENTHASHMAP + LONGADDER PARA FRECUENCIAS");

        ConcurrentHashMap<String, LongAdder> wordCounts = new ConcurrentHashMap<>();
        String[] words = {"java", "concurrent", "java", "atomic", "concurrent", "java", "thread", "safe"};

        result.add(MessageType.INFO, "Contando palabras: " + String.join(", ", words));

        for (String word : words) {
            wordCounts.computeIfAbsent(word, k -> new LongAdder()).increment();
        }

        result.add(MessageType.DEBUG, "Frecuencias:");
        wordCounts.forEach((word, count) ->
            result.add(MessageType.DEBUG, "  " + word + ": " + count.sum()));

        result.add(MessageType.INFO, "Simulando conteo concurrente con " + THREAD_COUNT + " threads:");
        ConcurrentHashMap<Character, LongAdder> letterCounts = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        String text = "ConcurrentHashMapWithLongAdderForHighPerformanceCounting";

        try {
            for (int t = 0; t < THREAD_COUNT; t++) {
                executor.execute(() -> {
                    for (char c : text.toCharArray()) {
                        letterCounts
                            .computeIfAbsent(c, k -> new LongAdder())
                            .increment();
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result.add(MessageType.DEBUG, "Conteo de letras (concurrente, " + THREAD_COUNT + " threads cada uno procesando '" + text + "'):");
        letterCounts.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue().sum(), e1.getValue().sum()))
            .limit(5)
            .forEach(entry -> result.add(MessageType.DEBUG,
                "  '" + entry.getKey() + "': " + entry.getValue().sum()));

        result.add(MessageType.SUCCESS, "Total de caracteres contados: " +
            letterCounts.values().stream().mapToLong(LongAdder::sum).sum());
    }

    private void explainWhenToUse(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "¿CUÁNDO USAR CADA UNO?");

        result.add(MessageType.INFO, "USA AtomicLong cuando:");
        result.add(MessageType.DEBUG, "  • Necesitas el valor exacto inmediatamente (get() es consistente)");
        result.add(MessageType.DEBUG, "  • Hay poca contención (pocos threads escribiendo)");
        result.add(MessageType.DEBUG, "  • El valor se lee frecuentemente (sum() de LongAdder es más costoso)");
        result.add(MessageType.DEBUG, "  • Necesitas operaciones atómicas complejas (CAS loops)");

        result.add(MessageType.INFO, "USA LongAdder cuando:");
        result.add(MessageType.DEBUG, "  • Hay alta contención (muchos threads escribiendo concurrentemente)");
        result.add(MessageType.DEBUG, "  • Las escrituras son mucho más frecuentes que las lecturas");
        result.add(MessageType.DEBUG, "  • Necesitas contadores de eventos o estadísticas");
        result.add(MessageType.DEBUG, "  • Puedes tolerar que sum() no sea 100% actualizado en tiempo real");

        result.add(MessageType.INFO, "Ejemplos de uso de LongAdder:");
        result.add(MessageType.DEBUG, "  • Contadores de requests HTTP por endpoint");
        result.add(MessageType.DEBUG, "  • Estadísticas de eventos (clicks, errores)");
        result.add(MessageType.DEBUG, "  • Métricas de rendimiento (latencias, throughput)");
        result.add(MessageType.DEBUG, "  • Conteo de palabras en procesamiento de texto concurrente");

        result.add(MessageType.MESSAGE, "Analogía: AtomicLong es como una caja fuerte única;");
        result.add(MessageType.MESSAGE, "LongAdder es como múltiples cajas que se consolidan al final.");
    }
}

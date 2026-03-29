package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedMapAdder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones de ConcurrentHashMap.
 */
public final class ConcurrentHashMapDemo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private ConcurrentHashMapDemo() {
    }

    /**
     * Muestra los constructores principales.
     */
    public static void showConstructors() {
        ConsolePrinter.print(MessageType.SUBTITLE, "CONSTRUCTORES");

        ConcurrentMap<Integer, String> defaultMap = new ConcurrentHashMap<>();
        defaultMap.put(1, "UNO");
        defaultMap.put(2, "DOS");
        defaultMap.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, defaultMap.toString());

        ConcurrentMap<Integer, String> mapWithInitialCapacity = new ConcurrentHashMap<>(3);
        mapWithInitialCapacity.put(1, "UNO");
        mapWithInitialCapacity.put(2, "DOS");
        mapWithInitialCapacity.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, mapWithInitialCapacity.toString());

        ConcurrentMap<Integer, String> mapWithLoadFactor = new ConcurrentHashMap<>(3, 0.75F);
        mapWithLoadFactor.put(1, "UNO");
        mapWithLoadFactor.put(2, "DOS");
        mapWithLoadFactor.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, mapWithLoadFactor.toString());

        ConcurrentMap<Integer, String> mapWithConcurrencyLevel = new ConcurrentHashMap<>(3, 0.75F, 4);
        mapWithConcurrencyLevel.put(1, "UNO");
        mapWithConcurrencyLevel.put(2, "DOS");
        mapWithConcurrencyLevel.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, mapWithConcurrencyLevel.toString());

        Map<Integer, String> sourceMap = new HashMap<>();
        sourceMap.put(1, "UNO");
        sourceMap.put(2, "DOS");
        sourceMap.put(3, "TRES");
        ConcurrentMap<Integer, String> mapFromMap = new ConcurrentHashMap<>(sourceMap);
        ConsolePrinter.print(MessageType.DEBUG, mapFromMap.toString());
    }

    /**
     * Compara el recorrido concurrente de HashMap y ConcurrentHashMap.
     */
    public static void demonstrateConcurrentIteration() {
        ConsolePrinter.print(MessageType.SUBTITLE, "HashMap - FAIL-FAST");

        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "UNO");
        hashMap.put(2, "DOS");
        hashMap.put(3, "TRES");

        ExecutorService hashMapExecutor = startAsyncAddition(new UnsynchronizedMapAdder(hashMap, 4, "CUATRO"));
        ConsolePrinter.printMap(hashMap);
        awaitCompletion(hashMapExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + hashMap);

        ConsolePrinter.print(MessageType.SUBTITLE, "ConcurrentHashMap - FAIL-SAFE");

        ConcurrentMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put(1, "UNO");
        concurrentMap.put(2, "DOS");
        concurrentMap.put(3, "TRES");

        ExecutorService concurrentMapExecutor = startAsyncAddition(
                new UnsynchronizedMapAdder(concurrentMap, 4, "CUATRO"));
        ConsolePrinter.printMap(concurrentMap);
        awaitCompletion(concurrentMapExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + concurrentMap);
    }

    private static ExecutorService startAsyncAddition(Runnable task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        executor.shutdown();
        return executor;
    }

    private static void awaitCompletion(ExecutorService executor) {
        try {
            if (!executor.awaitTermination(EXECUTOR_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                ConsolePrinter.print(MessageType.ERROR, "No fue posible finalizar el executor a tiempo");
                executor.shutdownNow();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            ConsolePrinter.print(MessageType.EXCEPTION, exception.toString());
        }
    }
}

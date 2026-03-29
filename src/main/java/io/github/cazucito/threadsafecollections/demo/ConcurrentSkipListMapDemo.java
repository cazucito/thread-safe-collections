package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedMapAdder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones de ConcurrentSkipListMap.
 */
public final class ConcurrentSkipListMapDemo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private ConcurrentSkipListMapDemo() {
    }

    /**
     * Muestra los constructores principales.
     */
    public static void showConstructors() {
        ConsolePrinter.print(MessageType.SUBTITLE, "CONSTRUCTORES");

        ConcurrentNavigableMap<Integer, String> emptyMap = new ConcurrentSkipListMap<>();
        emptyMap.put(1, "UNO");
        emptyMap.put(2, "DOS");
        emptyMap.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, emptyMap.toString());

        Comparator<Integer> reverseComparator = (left, right) -> Integer.compare(right, left);
        ConcurrentNavigableMap<Integer, String> mapWithComparator = new ConcurrentSkipListMap<>(reverseComparator);
        mapWithComparator.put(1, "UNO");
        mapWithComparator.put(2, "DOS");
        mapWithComparator.put(3, "TRES");
        ConsolePrinter.print(MessageType.DEBUG, mapWithComparator.toString());

        Map<Integer, String> hashMapData = new HashMap<>();
        hashMapData.put(1, "UNO");
        hashMapData.put(2, "DOS");
        hashMapData.put(3, "TRES");
        ConcurrentNavigableMap<Integer, String> mapFromMap = new ConcurrentSkipListMap<>(hashMapData);
        ConsolePrinter.print(MessageType.DEBUG, mapFromMap.toString());

        SortedMap<Integer, String> sortedMapData = new TreeMap<>(reverseComparator);
        sortedMapData.put(1, "UNO");
        sortedMapData.put(2, "DOS");
        sortedMapData.put(3, "TRES");
        sortedMapData.put(4, "CUATRO");
        ConcurrentNavigableMap<Integer, String> mapFromSortedMap = new ConcurrentSkipListMap<>(sortedMapData);
        ConsolePrinter.print(MessageType.DEBUG, mapFromSortedMap.toString());
    }

    /**
     * Compara el recorrido concurrente de TreeMap y ConcurrentSkipListMap.
     */
    public static void demonstrateConcurrentIteration() {
        ConsolePrinter.print(MessageType.SUBTITLE, "TreeMap - FAIL-FAST");

        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "UNO");
        treeMap.put(2, "DOS");
        treeMap.put(3, "TRES");

        ExecutorService treeMapExecutor = startAsyncAddition(new UnsynchronizedMapAdder(treeMap, 4, "CUATRO"));
        ConsolePrinter.printMap(treeMap);
        awaitCompletion(treeMapExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + treeMap);

        ConsolePrinter.print(MessageType.SUBTITLE, "ConcurrentSkipListMap - FAIL-SAFE");

        ConcurrentNavigableMap<Integer, String> concurrentMap = new ConcurrentSkipListMap<>();
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

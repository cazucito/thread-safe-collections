package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones de ConcurrentSkipListSet.
 */
public final class ConcurrentSkipListSetDemo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private ConcurrentSkipListSetDemo() {
    }

    /**
     * Muestra los constructores principales.
     */
    public static void showConstructors() {
        ConsolePrinter.print(MessageType.SUBTITLE, "CONSTRUCTORES");

        ConcurrentSkipListSet<String> emptySet = new ConcurrentSkipListSet<>();
        emptySet.add("Uno");
        emptySet.add("Dos");
        emptySet.add("Tres");
        ConsolePrinter.print(MessageType.DEBUG, emptySet.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        NavigableSet<String> setFromCollection = new ConcurrentSkipListSet<>(baseCollection);
        ConsolePrinter.print(MessageType.DEBUG, setFromCollection.toString());

        Comparator<String> lengthComparator = (left, right) -> Integer.compare(left.length(), right.length());
        NavigableSet<String> setWithComparator = new ConcurrentSkipListSet<>(lengthComparator);
        setWithComparator.add("4444");
        setWithComparator.add("333");
        setWithComparator.add("55555");
        setWithComparator.add("22");
        setWithComparator.add("22");
        ConsolePrinter.print(MessageType.DEBUG, setWithComparator.toString());

        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("Uno");
        sortedSet.add("Dos");
        sortedSet.add("Tres");
        sortedSet.add("Tres");
        ConcurrentSkipListSet<String> setFromSortedSet = new ConcurrentSkipListSet<>(sortedSet);
        ConsolePrinter.print(MessageType.DEBUG, setFromSortedSet.toString());
    }

    /**
     * Compara el recorrido concurrente de TreeSet y ConcurrentSkipListSet.
     */
    public static void demonstrateConcurrentIteration() {
        ConsolePrinter.print(MessageType.SUBTITLE, "SortedSet - FAIL-FAST");

        SortedSet<String> treeSet = new TreeSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        treeSet.add("Tres");
        ConsolePrinter.print(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService treeSetExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(treeSet, "CUATRO"));
        ConsolePrinter.printCollection(treeSet);
        awaitCompletion(treeSetExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + treeSet);

        ConsolePrinter.print(MessageType.SUBTITLE, "ConcurrentSkipListSet - FAIL-SAFE");

        ConcurrentSkipListSet<String> concurrentSet = new ConcurrentSkipListSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        concurrentSet.add("Tres");
        ConsolePrinter.print(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService concurrentSetExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(concurrentSet, "CUATRO"));
        ConsolePrinter.printCollection(concurrentSet);
        awaitCompletion(concurrentSetExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + concurrentSet);
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

package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones de CopyOnWriteArraySet.
 */
public final class CopyOnWriteArraySetDemo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private CopyOnWriteArraySetDemo() {
    }

    /**
     * Muestra los constructores principales.
     */
    public static void showConstructors() {
        ConsolePrinter.print(MessageType.SUBTITLE, "CONSTRUCTORES");

        CopyOnWriteArraySet<String> emptySet = new CopyOnWriteArraySet<>();
        emptySet.add("Uno");
        emptySet.add("Dos");
        emptySet.add("Tres");
        ConsolePrinter.print(MessageType.DEBUG, emptySet.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        CopyOnWriteArraySet<String> setFromCollection = new CopyOnWriteArraySet<>(baseCollection);
        ConsolePrinter.print(MessageType.DEBUG, setFromCollection.toString());
    }

    /**
     * Compara el recorrido concurrente de HashSet y CopyOnWriteArraySet.
     */
    public static void demonstrateConcurrentIteration() {
        ConsolePrinter.print(MessageType.SUBTITLE, "HashSet - FAIL-FAST");

        Set<String> hashSet = new HashSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        hashSet.add("Tres");
        ConsolePrinter.print(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService hashSetExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(hashSet, "CUATRO"));
        ConsolePrinter.printCollection(hashSet);
        awaitCompletion(hashSetExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + hashSet);

        ConsolePrinter.print(MessageType.SUBTITLE, "CopyOnWriteArraySet - FAIL-SAFE");

        CopyOnWriteArraySet<String> copyOnWriteSet = new CopyOnWriteArraySet<>(Arrays.asList("Uno", "Dos", "Tres"));
        copyOnWriteSet.add("Tres");
        ConsolePrinter.print(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService copyOnWriteExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(copyOnWriteSet, "CUATRO"));
        ConsolePrinter.printCollection(copyOnWriteSet);
        awaitCompletion(copyOnWriteExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + copyOnWriteSet);
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

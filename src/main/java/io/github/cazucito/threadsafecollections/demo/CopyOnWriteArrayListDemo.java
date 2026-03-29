package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones de CopyOnWriteArrayList.
 */
public final class CopyOnWriteArrayListDemo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private CopyOnWriteArrayListDemo() {
    }

    /**
     * Muestra los constructores principales.
     */
    public static void showConstructors() {
        ConsolePrinter.print(MessageType.SUBTITLE, "CONSTRUCTORES");

        CopyOnWriteArrayList<String> emptyList = new CopyOnWriteArrayList<>();
        emptyList.add("Uno");
        emptyList.add("Dos");
        emptyList.add("Tres");
        ConsolePrinter.print(MessageType.DEBUG, emptyList.toString());

        String[] words = {"Uno", "Dos", "Tres"};
        CopyOnWriteArrayList<String> listFromArray = new CopyOnWriteArrayList<>(words);
        ConsolePrinter.print(MessageType.DEBUG, listFromArray.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        CopyOnWriteArrayList<String> listFromCollection = new CopyOnWriteArrayList<>(baseCollection);
        ConsolePrinter.print(MessageType.DEBUG, listFromCollection.toString());
    }

    /**
     * Compara el recorrido concurrente de ArrayList y CopyOnWriteArrayList.
     */
    public static void demonstrateConcurrentIteration() {
        ConsolePrinter.print(MessageType.SUBTITLE, "ArrayList - FAIL-FAST");

        List<String> arrayList = new ArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
        ExecutorService arrayListExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(arrayList, "CUATRO"));
        ConsolePrinter.printCollection(arrayList);
        awaitCompletion(arrayListExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + arrayList);

        ConsolePrinter.print(MessageType.SUBTITLE, "CopyOnWriteArrayList - FAIL-SAFE");

        CopyOnWriteArrayList<String> copyOnWriteList = new CopyOnWriteArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
        ExecutorService copyOnWriteExecutor = startAsyncAddition(
                new UnsynchronizedCollectionAdder(copyOnWriteList, "CUATRO"));
        ConsolePrinter.printCollection(copyOnWriteList);
        awaitCompletion(copyOnWriteExecutor);
        ConsolePrinter.print(MessageType.MESSAGE, "Estado final: " + copyOnWriteList);
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

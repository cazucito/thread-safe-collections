package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.SynchronizedCollectionAdder;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demostraciones básicas con colecciones tradicionales.
 */
public final class BasicCollectionDemo {

    private static final int THREAD_COUNT = 7;
    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    private BasicCollectionDemo() {
    }

    /**
     * Demuestra una adición concurrente sin sincronización.
     */
    public static void demonstrateUnsynchronizedAddition() {
        ConsolePrinter.print(MessageType.SUBTITLE, "ArrayList - no sincronizado");

        List<String> collection = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new UnsynchronizedCollectionAdder(collection, String.valueOf(index)));
        }

        awaitCompletion(executor);
        printSizeResult(collection.size());
    }

    /**
     * Demuestra una adición concurrente con sincronización explícita.
     */
    public static void demonstrateSynchronizedAddition() {
        ConsolePrinter.print(MessageType.SUBTITLE, "ArrayList - synchronized");

        List<String> collection = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new SynchronizedCollectionAdder(collection, String.valueOf(index)));
        }

        awaitCompletion(executor);
        printSizeResult(collection.size());
    }

    /**
     * Demuestra una adición concurrente con la utilidad de sincronización de la JDK.
     */
    public static void demonstrateUtilitySynchronizedAddition() {
        ConsolePrinter.print(MessageType.SUBTITLE, "ArrayList - Collections.synchronizedList()");

        List<String> collection = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new UnsynchronizedCollectionAdder(collection, " " + index));
        }

        awaitCompletion(executor);
        printSizeResult(collection.size());
    }

    private static void printSizeResult(int elementCount) {
        if (THREAD_COUNT != elementCount) {
            ConsolePrinter.print(MessageType.LOGIC_ERROR, THREAD_COUNT + " \u2260 " + elementCount);
            return;
        }

        ConsolePrinter.print(MessageType.SUCCESS, THREAD_COUNT + " = " + elementCount);
    }

    private static void awaitCompletion(ExecutorService executor) {
        executor.shutdown();

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

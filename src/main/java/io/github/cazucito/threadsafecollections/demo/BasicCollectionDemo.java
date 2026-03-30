package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.concurrency.CompletionStatus;
import io.github.cazucito.threadsafecollections.concurrency.SynchronizedCollectionAdder;
import io.github.cazucito.threadsafecollections.concurrency.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demostraciones básicas con colecciones tradicionales.
 */
public final class BasicCollectionDemo implements Demo {

    private static final int THREAD_COUNT = 7;
    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "basic-collection";
    }

    @Override
    public String title() {
        return "COLECCIONES TRADICIONALES (java.util)";
    }

    @Override
    public String learningObjective() {
        return "Comparar inserciones concurrentes con y sin sincronización explícita.";
    }

    @Override
    public String expectedObservation() {
        return "La lista no sincronizada puede perder elementos; las protegidas no.";
    }

    @Override
    public String keyTakeaway() {
        return "Una colección tradicional necesita protección externa para uso concurrente.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        demonstrateUnsynchronizedAddition(result);
        demonstrateSynchronizedAddition(result);
        demonstrateUtilitySynchronizedAddition(result);

        return result.build();
    }

    private void demonstrateUnsynchronizedAddition(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - no sincronizado");

        List<String> collection = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new UnsynchronizedCollectionAdder(collection, String.valueOf(index)));
        }

        appendCompletionOutcome(result, executor);
        appendSizeResult(result, collection.size());
    }

    private void demonstrateSynchronizedAddition(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - synchronized");

        List<String> collection = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new SynchronizedCollectionAdder(collection, String.valueOf(index)));
        }

        appendCompletionOutcome(result, executor);
        appendSizeResult(result, collection.size());
    }

    private void demonstrateUtilitySynchronizedAddition(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - Collections.synchronizedList()");

        List<String> collection = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int index = 1; index <= THREAD_COUNT; index++) {
            executor.execute(new UnsynchronizedCollectionAdder(collection, " " + index));
        }

        appendCompletionOutcome(result, executor);
        appendSizeResult(result, collection.size());
    }

    private void appendCompletionOutcome(DemoResult.Builder result, ExecutorService executor) {
        executor.shutdown();
        CompletionStatus completionStatus = AsyncTaskSupport.awaitCompletion(executor, EXECUTOR_TIMEOUT_SECONDS);

        if (completionStatus == CompletionStatus.TIMED_OUT) {
            result.add(MessageType.ERROR, "No fue posible finalizar el executor a tiempo");
        } else if (completionStatus == CompletionStatus.INTERRUPTED) {
            result.add(MessageType.EXCEPTION, "La espera del executor fue interrumpida");
        }
    }

    private void appendSizeResult(DemoResult.Builder result, int elementCount) {
        if (THREAD_COUNT != elementCount) {
            result.add(MessageType.LOGIC_ERROR, THREAD_COUNT + " != " + elementCount);
            return;
        }

        result.add(MessageType.SUCCESS, THREAD_COUNT + " = " + elementCount);
    }
}

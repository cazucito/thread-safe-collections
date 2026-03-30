package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.concurrency.CompletionStatus;
import io.github.cazucito.threadsafecollections.concurrency.UnsynchronizedMapAdder;
import io.github.cazucito.threadsafecollections.traversal.MapTraversal;
import io.github.cazucito.threadsafecollections.traversal.TraversalCapture;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

/**
 * Demostraciones de ConcurrentHashMap.
 */
public final class ConcurrentHashMapDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "concurrent-hash-map";
    }

    @Override
    public String title() {
        return "ConcurrentHashMap (java.util.concurrent)";
    }

    @Override
    public String learningObjective() {
        return "Comparar un mapa tradicional con uno diseñado para concurrencia.";
    }

    @Override
    public String expectedObservation() {
        return "HashMap puede fallar al iterar; ConcurrentHashMap sigue operativo.";
    }

    @Override
    public String keyTakeaway() {
        return "ConcurrentHashMap suele ser la alternativa práctica para mapas compartidos.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        showConstructors(result);
        demonstrateConcurrentIteration(result);

        return result.build();
    }

    private void showConstructors(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "CONSTRUCTORES");

        ConcurrentMap<Integer, String> defaultMap = new ConcurrentHashMap<>();
        defaultMap.put(1, "UNO");
        defaultMap.put(2, "DOS");
        defaultMap.put(3, "TRES");
        result.add(MessageType.DEBUG, defaultMap.toString());

        ConcurrentMap<Integer, String> mapWithInitialCapacity = new ConcurrentHashMap<>(3);
        mapWithInitialCapacity.put(1, "UNO");
        mapWithInitialCapacity.put(2, "DOS");
        mapWithInitialCapacity.put(3, "TRES");
        result.add(MessageType.DEBUG, mapWithInitialCapacity.toString());

        ConcurrentMap<Integer, String> mapWithLoadFactor = new ConcurrentHashMap<>(3, 0.75F);
        mapWithLoadFactor.put(1, "UNO");
        mapWithLoadFactor.put(2, "DOS");
        mapWithLoadFactor.put(3, "TRES");
        result.add(MessageType.DEBUG, mapWithLoadFactor.toString());

        ConcurrentMap<Integer, String> mapWithConcurrencyLevel = new ConcurrentHashMap<>(3, 0.75F, 4);
        mapWithConcurrencyLevel.put(1, "UNO");
        mapWithConcurrencyLevel.put(2, "DOS");
        mapWithConcurrencyLevel.put(3, "TRES");
        result.add(MessageType.DEBUG, mapWithConcurrencyLevel.toString());

        Map<Integer, String> sourceMap = new HashMap<>();
        sourceMap.put(1, "UNO");
        sourceMap.put(2, "DOS");
        sourceMap.put(3, "TRES");
        ConcurrentMap<Integer, String> mapFromMap = new ConcurrentHashMap<>(sourceMap);
        result.add(MessageType.DEBUG, mapFromMap.toString());
    }

    private void demonstrateConcurrentIteration(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "HashMap - FAIL-FAST");

        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "UNO");
        hashMap.put(2, "DOS");
        hashMap.put(3, "TRES");

        ExecutorService hashMapExecutor = AsyncTaskSupport.startSingleTask(new UnsynchronizedMapAdder(hashMap, 4,
                "CUATRO"));
        appendTraversalMessages(result, MapTraversal.capture(hashMap));
        appendCompletionOutcome(result, hashMapExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + hashMap);

        result.add(MessageType.SUBTITLE, "ConcurrentHashMap - FAIL-SAFE");

        ConcurrentMap<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put(1, "UNO");
        concurrentMap.put(2, "DOS");
        concurrentMap.put(3, "TRES");

        ExecutorService concurrentMapExecutor = AsyncTaskSupport.startSingleTask(new UnsynchronizedMapAdder(
                concurrentMap, 4, "CUATRO"));
        appendTraversalMessages(result, MapTraversal.capture(concurrentMap));
        appendCompletionOutcome(result, concurrentMapExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + concurrentMap);
    }

    private void appendTraversalMessages(DemoResult.Builder result, TraversalCapture capture) {
        capture.exceptionMessage().ifPresent(message -> result.add(MessageType.EXCEPTION, message));
        result.add(MessageType.DEBUG, "finally: " + capture.renderedState());
    }

    private void appendCompletionOutcome(DemoResult.Builder result, ExecutorService executor) {
        CompletionStatus completionStatus = AsyncTaskSupport.awaitCompletion(executor, EXECUTOR_TIMEOUT_SECONDS);
        if (completionStatus == CompletionStatus.TIMED_OUT) {
            result.add(MessageType.ERROR, "No fue posible finalizar el executor a tiempo");
        } else if (completionStatus == CompletionStatus.INTERRUPTED) {
            result.add(MessageType.EXCEPTION, "La espera del executor fue interrumpida");
        }
    }
}

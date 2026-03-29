package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.support.CompletionStatus;
import io.github.cazucito.threadsafecollections.support.MapTraversal;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.TraversalCapture;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedMapAdder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;

/**
 * Demostraciones de ConcurrentSkipListMap.
 */
public final class ConcurrentSkipListMapDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "concurrent-skip-list-map";
    }

    @Override
    public String title() {
        return "ConcurrentSkipListMap (java.util.concurrent)";
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

        ConcurrentNavigableMap<Integer, String> emptyMap = new ConcurrentSkipListMap<>();
        emptyMap.put(1, "UNO");
        emptyMap.put(2, "DOS");
        emptyMap.put(3, "TRES");
        result.add(MessageType.DEBUG, emptyMap.toString());

        Comparator<Integer> reverseComparator = (left, right) -> Integer.compare(right, left);
        ConcurrentNavigableMap<Integer, String> mapWithComparator = new ConcurrentSkipListMap<>(reverseComparator);
        mapWithComparator.put(1, "UNO");
        mapWithComparator.put(2, "DOS");
        mapWithComparator.put(3, "TRES");
        result.add(MessageType.DEBUG, mapWithComparator.toString());

        Map<Integer, String> hashMapData = new HashMap<>();
        hashMapData.put(1, "UNO");
        hashMapData.put(2, "DOS");
        hashMapData.put(3, "TRES");
        ConcurrentNavigableMap<Integer, String> mapFromMap = new ConcurrentSkipListMap<>(hashMapData);
        result.add(MessageType.DEBUG, mapFromMap.toString());

        SortedMap<Integer, String> sortedMapData = new TreeMap<>(reverseComparator);
        sortedMapData.put(1, "UNO");
        sortedMapData.put(2, "DOS");
        sortedMapData.put(3, "TRES");
        sortedMapData.put(4, "CUATRO");
        ConcurrentNavigableMap<Integer, String> mapFromSortedMap = new ConcurrentSkipListMap<>(sortedMapData);
        result.add(MessageType.DEBUG, mapFromSortedMap.toString());
    }

    private void demonstrateConcurrentIteration(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "TreeMap - FAIL-FAST");

        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "UNO");
        treeMap.put(2, "DOS");
        treeMap.put(3, "TRES");

        ExecutorService treeMapExecutor = AsyncTaskSupport.startSingleTask(new UnsynchronizedMapAdder(treeMap, 4,
                "CUATRO"));
        appendTraversalMessages(result, MapTraversal.capture(treeMap));
        appendCompletionOutcome(result, treeMapExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + treeMap);

        result.add(MessageType.SUBTITLE, "ConcurrentSkipListMap - FAIL-SAFE");

        ConcurrentNavigableMap<Integer, String> concurrentMap = new ConcurrentSkipListMap<>();
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

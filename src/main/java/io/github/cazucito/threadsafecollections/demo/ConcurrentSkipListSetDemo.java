package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.concurrency.CompletionStatus;
import io.github.cazucito.threadsafecollections.concurrency.UnsynchronizedCollectionAdder;
import io.github.cazucito.threadsafecollections.traversal.CollectionTraversal;
import io.github.cazucito.threadsafecollections.traversal.TraversalCapture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

/**
 * Demostraciones de ConcurrentSkipListSet.
 */
public final class ConcurrentSkipListSetDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "concurrent-skip-list-set";
    }

    @Override
    public String title() {
        return "ConcurrentSkipListSet (java.util.concurrent)";
    }

    @Override
    public String learningObjective() {
        return "Comparar un conjunto ordenado tradicional con uno concurrente.";
    }

    @Override
    public String expectedObservation() {
        return "TreeSet puede fallar; ConcurrentSkipListSet permite recorrido concurrente.";
    }

    @Override
    public String keyTakeaway() {
        return "ConcurrentSkipListSet combina orden natural con acceso concurrente seguro.";
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

        ConcurrentSkipListSet<String> emptySet = new ConcurrentSkipListSet<>();
        emptySet.add("Uno");
        emptySet.add("Dos");
        emptySet.add("Tres");
        result.add(MessageType.DEBUG, emptySet.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        NavigableSet<String> setFromCollection = new ConcurrentSkipListSet<>(baseCollection);
        result.add(MessageType.DEBUG, setFromCollection.toString());

        Comparator<String> lengthComparator = (left, right) -> Integer.compare(left.length(), right.length());
        NavigableSet<String> setWithComparator = new ConcurrentSkipListSet<>(lengthComparator);
        setWithComparator.add("4444");
        setWithComparator.add("333");
        setWithComparator.add("55555");
        setWithComparator.add("22");
        setWithComparator.add("22");
        result.add(MessageType.DEBUG, setWithComparator.toString());

        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("Uno");
        sortedSet.add("Dos");
        sortedSet.add("Tres");
        sortedSet.add("Tres");
        ConcurrentSkipListSet<String> setFromSortedSet = new ConcurrentSkipListSet<>(sortedSet);
        result.add(MessageType.DEBUG, setFromSortedSet.toString());
    }

    private void demonstrateConcurrentIteration(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "SortedSet - FAIL-FAST");

        SortedSet<String> treeSet = new TreeSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        treeSet.add("Tres");
        result.add(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService treeSetExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(treeSet, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(treeSet));
        appendCompletionOutcome(result, treeSetExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + treeSet);

        result.add(MessageType.SUBTITLE, "ConcurrentSkipListSet - FAIL-SAFE");

        ConcurrentSkipListSet<String> concurrentSet = new ConcurrentSkipListSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        concurrentSet.add("Tres");
        result.add(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService concurrentSetExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(concurrentSet, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(concurrentSet));
        appendCompletionOutcome(result, concurrentSetExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + concurrentSet);
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

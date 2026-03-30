package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.support.CollectionTraversal;
import io.github.cazucito.threadsafecollections.support.CompletionStatus;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.TraversalCapture;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;

/**
 * Demostraciones de CopyOnWriteArraySet.
 */
public final class CopyOnWriteArraySetDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "copy-on-write-array-set";
    }

    @Override
    public String title() {
        return "CopyOnWriteArraySet (java.util.concurrent)";
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

        CopyOnWriteArraySet<String> emptySet = new CopyOnWriteArraySet<>();
        emptySet.add("Uno");
        emptySet.add("Dos");
        emptySet.add("Tres");
        result.add(MessageType.DEBUG, emptySet.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        CopyOnWriteArraySet<String> setFromCollection = new CopyOnWriteArraySet<>(baseCollection);
        result.add(MessageType.DEBUG, setFromCollection.toString());
    }

    private void demonstrateConcurrentIteration(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "HashSet - FAIL-FAST");

        Set<String> hashSet = new HashSet<>(Arrays.asList("Uno", "Dos", "Tres"));
        hashSet.add("Tres");
        result.add(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService hashSetExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(hashSet, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(hashSet));
        appendCompletionOutcome(result, hashSetExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + hashSet);

        result.add(MessageType.SUBTITLE, "CopyOnWriteArraySet - FAIL-SAFE");

        CopyOnWriteArraySet<String> copyOnWriteSet = new CopyOnWriteArraySet<>(Arrays.asList("Uno", "Dos", "Tres"));
        copyOnWriteSet.add("Tres");
        result.add(MessageType.SUCCESS, ".add(\"Tres\") no altera la colección");

        ExecutorService copyOnWriteExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(copyOnWriteSet, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(copyOnWriteSet));
        appendCompletionOutcome(result, copyOnWriteExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + copyOnWriteSet);
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

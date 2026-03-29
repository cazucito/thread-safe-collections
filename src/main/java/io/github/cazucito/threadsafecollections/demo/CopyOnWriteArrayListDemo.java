package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.AsyncTaskSupport;
import io.github.cazucito.threadsafecollections.support.CollectionTraversal;
import io.github.cazucito.threadsafecollections.support.CompletionStatus;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.TraversalCapture;
import io.github.cazucito.threadsafecollections.support.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Demostraciones de CopyOnWriteArrayList.
 */
public final class CopyOnWriteArrayListDemo implements Demo {

    private static final int EXECUTOR_TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "copy-on-write-array-list";
    }

    @Override
    public String title() {
        return "CopyOnWriteArrayList (java.util.concurrent)";
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

        CopyOnWriteArrayList<String> emptyList = new CopyOnWriteArrayList<>();
        emptyList.add("Uno");
        emptyList.add("Dos");
        emptyList.add("Tres");
        result.add(MessageType.DEBUG, emptyList.toString());

        String[] words = {"Uno", "Dos", "Tres"};
        CopyOnWriteArrayList<String> listFromArray = new CopyOnWriteArrayList<>(words);
        result.add(MessageType.DEBUG, listFromArray.toString());

        List<String> baseCollection = new ArrayList<>();
        baseCollection.add("Uno");
        baseCollection.add("Dos");
        baseCollection.add("Tres");
        CopyOnWriteArrayList<String> listFromCollection = new CopyOnWriteArrayList<>(baseCollection);
        result.add(MessageType.DEBUG, listFromCollection.toString());
    }

    private void demonstrateConcurrentIteration(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - FAIL-FAST");

        List<String> arrayList = new ArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
        ExecutorService arrayListExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(arrayList, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(arrayList));
        appendCompletionOutcome(result, arrayListExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + arrayList);

        result.add(MessageType.SUBTITLE, "CopyOnWriteArrayList - FAIL-SAFE");

        CopyOnWriteArrayList<String> copyOnWriteList = new CopyOnWriteArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
        ExecutorService copyOnWriteExecutor = AsyncTaskSupport.startSingleTask(
                new UnsynchronizedCollectionAdder(copyOnWriteList, "CUATRO"));
        appendTraversalMessages(result, CollectionTraversal.capture(copyOnWriteList));
        appendCompletionOutcome(result, copyOnWriteExecutor);
        result.add(MessageType.MESSAGE, "Estado final: " + copyOnWriteList);
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

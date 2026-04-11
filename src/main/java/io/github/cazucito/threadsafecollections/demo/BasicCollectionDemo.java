package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.ConcurrentTestScenario;
import io.github.cazucito.threadsafecollections.concurrency.SynchronizedCollectionAdder;
import io.github.cazucito.threadsafecollections.concurrency.UnsynchronizedCollectionAdder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demostraciones básicas con colecciones tradicionales.
 */
public final class BasicCollectionDemo implements Demo {

    private static final int THREAD_COUNT = 7;
    private static final int TIMEOUT_SECONDS = 2;

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
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(THREAD_COUNT, TIMEOUT_SECONDS);
        
        scenario.executeWithCollection(result, collection, 
            (coll, index) -> new UnsynchronizedCollectionAdder(coll, String.valueOf(index)));
        
        ConcurrentTestScenario.appendSizeResult(result, THREAD_COUNT, collection.size());
    }

    private void demonstrateSynchronizedAddition(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - synchronized");

        List<String> collection = new ArrayList<>();
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(THREAD_COUNT, TIMEOUT_SECONDS);
        
        scenario.executeWithCollection(result, collection,
            (coll, index) -> new SynchronizedCollectionAdder(coll, String.valueOf(index)));
        
        ConcurrentTestScenario.appendSizeResult(result, THREAD_COUNT, collection.size());
    }

    private void demonstrateUtilitySynchronizedAddition(DemoResult.Builder result) {
        result.add(MessageType.SUBTITLE, "ArrayList - Collections.synchronizedList()");

        List<String> collection = Collections.synchronizedList(new ArrayList<>());
        ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(THREAD_COUNT, TIMEOUT_SECONDS);
        
        scenario.executeWithCollection(result, collection,
            (coll, index) -> new UnsynchronizedCollectionAdder(coll, " " + index));
        
        ConcurrentTestScenario.appendSizeResult(result, THREAD_COUNT, collection.size());
    }


}

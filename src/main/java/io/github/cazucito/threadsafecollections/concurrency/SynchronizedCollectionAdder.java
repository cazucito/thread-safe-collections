package io.github.cazucito.threadsafecollections.concurrency;

import java.util.Collection;

/**
 * Agrega un elemento a una colección con sincronización explícita.
 */
public final class SynchronizedCollectionAdder implements Runnable {

    private final Collection<String> collection;
    private final String value;

    public SynchronizedCollectionAdder(Collection<String> collection, String value) {
        this.collection = collection;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            synchronized (collection) {
                ThreadPause.sleepMillis(100);
                collection.add(value);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible agregar '" + value + "' a la colección sincronizada",
                    exception);
        }
    }
}

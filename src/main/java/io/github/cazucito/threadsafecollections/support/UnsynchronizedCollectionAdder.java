package io.github.cazucito.threadsafecollections.support;

import java.util.Collection;

/**
 * Agrega un elemento a una colección sin sincronización explícita.
 */
public final class UnsynchronizedCollectionAdder implements Runnable {

    private final Collection<String> collection;
    private final String value;

    public UnsynchronizedCollectionAdder(Collection<String> collection, String value) {
        this.collection = collection;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            ThreadPause.sleepMillis(100);
            collection.add(value);
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible agregar '" + value + "' a la colección", exception);
        }
    }
}

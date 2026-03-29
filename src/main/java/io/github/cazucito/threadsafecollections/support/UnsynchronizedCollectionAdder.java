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
        String collectionState = "[]";

        try {
            ThreadPause.sleepMillis(100);
            collection.add(value);
            collectionState = collection.toString();
        } catch (Exception exception) {
            ConsolePrinter.print(MessageType.EXCEPTION, exception.toString());
        } finally {
            ConsolePrinter.print(MessageType.DEBUG,
                    "adición de '" + value + "' a la colección: " + collectionState);
        }
    }
}

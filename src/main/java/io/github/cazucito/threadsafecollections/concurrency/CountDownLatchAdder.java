package io.github.cazucito.threadsafecollections.concurrency;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**
 * Agrega un elemento a una colección usando CountDownLatch para sincronización determinística.
 * Útil para tests que requieren comportamiento concurrente predecible.
 */
public final class CountDownLatchAdder implements Runnable {

    private final Collection<String> collection;
    private final String value;
    private final CountDownLatch startLatch;
    private final CountDownLatch completeLatch;

    public CountDownLatchAdder(Collection<String> collection, String value, 
                               CountDownLatch startLatch, CountDownLatch completeLatch) {
        this.collection = collection;
        this.value = value;
        this.startLatch = startLatch;
        this.completeLatch = completeLatch;
    }

    @Override
    public void run() {
        try {
            // Esperar a que todas las threads estén listas
            startLatch.countDown();
            startLatch.await();
            
            // Todas las threads intentan agregar simultáneamente
            collection.add(value);
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible agregar '" + value + "' a la colección", exception);
        } finally {
            completeLatch.countDown();
        }
    }
}

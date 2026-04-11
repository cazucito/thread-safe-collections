package io.github.cazucito.threadsafecollections.concurrency;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Agrega un par llave-valor a un mapa usando CountDownLatch para sincronización determinística.
 * Útil para tests que requieren comportamiento concurrente predecible.
 */
public final class CountDownLatchMapAdder implements Runnable {

    private final Map<Integer, String> map;
    private final Integer key;
    private final String value;
    private final CountDownLatch startLatch;
    private final CountDownLatch completeLatch;

    public CountDownLatchMapAdder(Map<Integer, String> map, Integer key, String value,
                                  CountDownLatch startLatch, CountDownLatch completeLatch) {
        this.map = map;
        this.key = key;
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
            map.put(key, value);
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible agregar '" + key + "/" + value + "' al mapa", exception);
        } finally {
            completeLatch.countDown();
        }
    }
}

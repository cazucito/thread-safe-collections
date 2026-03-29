package io.github.cazucito.threadsafecollections.support;

import java.util.Map;

/**
 * Agrega un par llave-valor a un mapa sin sincronización explícita.
 */
public final class UnsynchronizedMapAdder implements Runnable {

    private final Map<Integer, String> map;
    private final Integer key;
    private final String value;

    public UnsynchronizedMapAdder(Map<Integer, String> map, Integer key, String value) {
        this.map = map;
        this.key = key;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            ThreadPause.sleepMillis(1);
            map.put(key, value);
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible agregar '" + key + "/" + value + "' al mapa",
                    exception);
        }
    }
}

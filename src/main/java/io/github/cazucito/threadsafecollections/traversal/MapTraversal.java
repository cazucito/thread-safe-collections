package io.github.cazucito.threadsafecollections.traversal;

import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import java.util.Map;

/**
 * Captura el recorrido de un mapa de enteros a cadenas.
 */
public final class MapTraversal {

    private MapTraversal() {
    }

    /**
     * Recorre un mapa y devuelve el estado observado.
     *
     * @param map mapa a recorrer
     * @return captura del recorrido
     */
    public static TraversalCapture capture(Map<Integer, String> map) {
        StringBuilder builder = new StringBuilder();

        try {
            builder.append("<");
            for (Integer key : map.keySet()) {
                builder.append(" ").append(key).append("/").append(map.get(key));
                ThreadPause.sleepMillis(100);
            }
            builder.append(" >");
            return TraversalCapture.success(builder.toString());
        } catch (Exception exception) {
            return TraversalCapture.failure(exception.toString(), builder.toString());
        }
    }
}

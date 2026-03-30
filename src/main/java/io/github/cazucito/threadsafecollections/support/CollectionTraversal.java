package io.github.cazucito.threadsafecollections.support;

import java.util.Collection;

/**
 * Captura el recorrido de una colección de cadenas.
 */
public final class CollectionTraversal {

    private CollectionTraversal() {
    }

    /**
     * Recorre una colección y devuelve el estado observado.
     *
     * @param collection colección a recorrer
     * @return captura del recorrido
     */
    public static TraversalCapture capture(Collection<String> collection) {
        StringBuilder builder = new StringBuilder();

        try {
            builder.append("<");
            for (String value : collection) {
                builder.append(" ").append(value);
                ThreadPause.sleepMillis(100);
            }
            builder.append(" >");
            return TraversalCapture.success(builder.toString());
        } catch (Exception exception) {
            return TraversalCapture.failure(exception.toString(), builder.toString());
        }
    }
}

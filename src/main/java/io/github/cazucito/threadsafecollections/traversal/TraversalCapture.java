package io.github.cazucito.threadsafecollections.traversal;

import java.util.Optional;

/**
 * Resultado de intentar recorrer una colección o mapa mientras otra tarea puede modificarlo.
 *
 * @param exceptionMessage excepción capturada, si ocurrió
 * @param renderedState    estado recorrido hasta el punto disponible
 */
public record TraversalCapture(Optional<String> exceptionMessage, String renderedState) {

    /**
     * Crea una captura exitosa.
     *
     * @param renderedState estado renderizado
     * @return captura sin excepción
     */
    public static TraversalCapture success(String renderedState) {
        return new TraversalCapture(Optional.empty(), renderedState);
    }

    /**
     * Crea una captura con excepción.
     *
     * @param exceptionMessage excepción capturada
     * @param renderedState    estado renderizado parcial
     * @return captura con excepción
     */
    public static TraversalCapture failure(String exceptionMessage, String renderedState) {
        return new TraversalCapture(Optional.ofNullable(exceptionMessage), renderedState);
    }
}

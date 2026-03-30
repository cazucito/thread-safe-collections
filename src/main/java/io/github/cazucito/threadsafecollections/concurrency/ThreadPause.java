package io.github.cazucito.threadsafecollections.concurrency;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilería de pausas para facilitar la reproducción de las demos.
 */
public final class ThreadPause {

    private ThreadPause() {
    }

    /**
     * Pausa el hilo actual por una cantidad fija de milisegundos.
     *
     * @param milliseconds tiempo de pausa
     */
    public static void sleepMillis(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pausa el hilo actual por un tiempo aleatorio dentro de un rango.
     *
     * @param minMilliseconds mínimo inclusivo
     * @param maxMilliseconds máximo exclusivo
     */
    public static void sleepBetween(int minMilliseconds, int maxMilliseconds) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(minMilliseconds, maxMilliseconds));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}

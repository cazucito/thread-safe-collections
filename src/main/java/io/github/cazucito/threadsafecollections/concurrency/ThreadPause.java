package io.github.cazucito.threadsafecollections.concurrency;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilería de pausas para facilitar la reproducción de las demos.
 */
public final class ThreadPause {

    private static volatile double delayMultiplier = 1.0d;

    private ThreadPause() {
    }

    /**
     * Devuelve el multiplicador activo para las pausas.
     *
     * @return multiplicador configurado
     */
    public static double delayMultiplier() {
        return delayMultiplier;
    }

    /**
     * Ajusta el multiplicador usado por las pausas.
     *
     * @param newDelayMultiplier multiplicador positivo
     */
    public static void setDelayMultiplier(double newDelayMultiplier) {
        if (newDelayMultiplier <= 0) {
            throw new IllegalArgumentException("El multiplicador de pausa debe ser positivo.");
        }

        delayMultiplier = newDelayMultiplier;
    }

    /**
     * Pausa el hilo actual por una cantidad fija de milisegundos.
     *
     * @param milliseconds tiempo de pausa
     */
    public static void sleepMillis(int milliseconds) {
        int scaledMilliseconds = scaleMilliseconds(milliseconds);
        if (scaledMilliseconds <= 0) {
            return;
        }

        try {
            Thread.sleep(scaledMilliseconds);
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
        int scaledMinMilliseconds = scaleMilliseconds(minMilliseconds);
        int scaledMaxMilliseconds = scaleMilliseconds(maxMilliseconds);

        if (scaledMaxMilliseconds <= scaledMinMilliseconds) {
            sleepMillis(scaledMinMilliseconds);
            return;
        }

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(scaledMinMilliseconds, scaledMaxMilliseconds));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private static int scaleMilliseconds(int milliseconds) {
        if (milliseconds <= 0) {
            return 0;
        }

        long scaledMilliseconds = Math.round(milliseconds * delayMultiplier);
        return (int) Math.max(1L, scaledMilliseconds);
    }
}

package io.github.cazucito.threadsafecollections.support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utilerías para ejecutar y esperar tareas asíncronas.
 */
public final class AsyncTaskSupport {

    private AsyncTaskSupport() {
    }

    /**
     * Inicia una tarea individual en un executor dedicado.
     *
     * @param task tarea a ejecutar
     * @return executor apagado y listo para ser esperado
     */
    public static ExecutorService startSingleTask(Runnable task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        executor.shutdown();
        return executor;
    }

    /**
     * Espera a que un executor termine.
     *
     * @param executor       executor a esperar
     * @param timeoutSeconds tiempo máximo en segundos
     * @return estado final de la espera
     */
    public static CompletionStatus awaitCompletion(ExecutorService executor, int timeoutSeconds) {
        try {
            if (executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                return CompletionStatus.COMPLETED;
            }

            executor.shutdownNow();
            return CompletionStatus.TIMED_OUT;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return CompletionStatus.INTERRUPTED;
        }
    }
}

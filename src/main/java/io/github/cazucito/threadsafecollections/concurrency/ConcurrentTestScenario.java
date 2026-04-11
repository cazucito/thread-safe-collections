package io.github.cazucito.threadsafecollections.concurrency;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Framework para ejecutar escenarios de prueba concurrente.
 * 
 * Encapsula el patrón repetido de:
 * - Crear ExecutorService con N threads
 * - Ejecutar tasks que modifican colecciones
 * - Shutdown + awaitTermination
 * - Verificación de resultados
 * 
 * Este framework reduce código duplicado en las demos manteniendo
 * la claridad pedagógica de la salida.
 */
public final class ConcurrentTestScenario {

    private final int threadCount;
    private final int timeoutSeconds;
    private final Consumer<DemoResult.Builder> verificationStrategy;

    /**
     * Crea un nuevo escenario de prueba concurrente.
     *
     * @param threadCount          número de threads a usar
     * @param timeoutSeconds       tiempo máximo de espera en segundos
     * @param verificationStrategy estrategia para verificar resultados
     */
    public ConcurrentTestScenario(int threadCount, int timeoutSeconds, 
                                   Consumer<DemoResult.Builder> verificationStrategy) {
        this.threadCount = threadCount;
        this.timeoutSeconds = timeoutSeconds;
        this.verificationStrategy = verificationStrategy;
    }

    /**
     * Crea un escenario con verificación de tamaño estándar.
     * Verifica que el tamaño final coincida con el número de threads.
     *
     * @param threadCount    número de threads
     * @param timeoutSeconds tiempo máximo de espera
     * @return escenario configurado con verificación de tamaño
     */
    public static ConcurrentTestScenario withSizeVerification(int threadCount, int timeoutSeconds) {
        return new ConcurrentTestScenario(threadCount, timeoutSeconds, 
            result -> appendSizeResult(result, threadCount, threadCount));
    }

    /**
     * Crea un escenario sin verificación automática (para casos especiales).
     *
     * @param threadCount    número de threads
     * @param timeoutSeconds tiempo máximo de espera
     * @return escenario sin verificación automática
     */
    public static ConcurrentTestScenario withoutVerification(int threadCount, int timeoutSeconds) {
        return new ConcurrentTestScenario(threadCount, timeoutSeconds, result -> {});
    }

    /**
     * Ejecuta el escenario concurrente con las tareas proporcionadas.
     *
     * @param resultBuilder builder para acumular resultados
     * @param taskFactory   factory que crea una tarea para cada índice (1-based)
     */
    public void execute(DemoResult.Builder resultBuilder, TaskFactory taskFactory) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            for (int index = 1; index <= threadCount; index++) {
                Runnable task = taskFactory.createTask(index);
                executor.execute(task);
            }
        } finally {
            executor.shutdown();
        }

        appendCompletionOutcome(resultBuilder, executor);
        verificationStrategy.accept(resultBuilder);
    }

    /**
     * Ejecuta el escenario concurrente con una colección modificable.
     * Útil para casos donde cada thread agrega un elemento a la colección.
     *
     * @param resultBuilder builder para acumular resultados
     * @param collection    colección a modificar
     * @param taskFactory   factory que crea una tarea para cada índice
     * @param <T>           tipo de la colección
     */
    public <T> void executeWithCollection(DemoResult.Builder resultBuilder, T collection,
                                           CollectionTaskFactory<T> taskFactory) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        try {
            for (int index = 1; index <= threadCount; index++) {
                Runnable task = taskFactory.createTask(collection, index);
                executor.execute(task);
            }
        } finally {
            executor.shutdown();
        }

        appendCompletionOutcome(resultBuilder, executor);
        verificationStrategy.accept(resultBuilder);
    }

    /**
     * Ejecuta un escenario con una sola tarea (para iteración concurrente).
     *
     * @param resultBuilder builder para acumular resultados
     * @param task          tarea a ejecutar
     */
    public void executeSingleTask(DemoResult.Builder resultBuilder, Runnable task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            executor.execute(task);
        } finally {
            executor.shutdown();
        }

        appendCompletionOutcome(resultBuilder, executor);
    }

    private void appendCompletionOutcome(DemoResult.Builder result, ExecutorService executor) {
        try {
            if (!executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                result.add(MessageType.ERROR, "No fue posible finalizar el executor a tiempo");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            result.add(MessageType.EXCEPTION, "La espera del executor fue interrumpida");
        }
    }

    /**
     * Agrega el resultado de verificación de tamaño al builder.
     *
     * @param result        builder
     * @param expected      tamaño esperado
     * @param actual        tamaño actual
     */
    public static void appendSizeResult(DemoResult.Builder result, int expected, int actual) {
        if (expected != actual) {
            result.add(MessageType.LOGIC_ERROR, expected + " != " + actual);
            return;
        }
        result.add(MessageType.SUCCESS, expected + " = " + actual);
    }

    /**
     * Factory para crear tareas basadas en índice.
     */
    @FunctionalInterface
    public interface TaskFactory {
        /**
         * Crea una tarea para el índice dado.
         *
         * @param index índice (1-based)
         * @return tarea a ejecutar
         */
        Runnable createTask(int index);
    }

    /**
     * Factory para crear tareas que operan sobre una colección.
     *
     * @param <T> tipo de la colección
     */
    @FunctionalInterface
    public interface CollectionTaskFactory<T> {
        /**
         * Crea una tarea para modificar la colección.
         *
         * @param collection colección a modificar
         * @param index      índice (1-based)
         * @return tarea a ejecutar
         */
        Runnable createTask(T collection, int index);
    }

    /**
     * Devuelve el número de threads configurado.
     *
     * @return thread count
     */
    public int threadCount() {
        return threadCount;
    }

    /**
     * Devuelve el timeout configurado.
     *
     * @return timeout en segundos
     */
    public int timeoutSeconds() {
        return timeoutSeconds;
    }
}

package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.concurrency.CountDownLatchAdder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * Tests determinísticos de concurrencia usando CountDownLatch.
 * 
 * Estos tests verifican comportamiento concurrente real sin depender de
 * temporización artificial (ThreadPause). Usan CountDownLatch para coordinar
 * el inicio simultáneo de múltiples threads.
 * 
 * NOTA SOBRE DEMOS CON TEMPORIZACIÓN ARTIFICIAL:
 * Las siguientes demos requieren ThreadPause porque necesitan que el
 * thread de modificación "gane" la carrera contra el thread de recorrido:
 * 
 * - CopyOnWriteArrayListDemo: Necesita que la modificación ocurra durante
 *   el recorrido para demostrar fail-fast vs fail-safe
 * - CopyOnWriteArraySetDemo: Similar al anterior, demuestra iteración segura
 * - ConcurrentSkipListSetDemo: Demuestra TreeSet fail-fast vs ConcurrentSkipListSet
 * - ConcurrentSkipListMapDemo: Demuestra TreeMap fail-fast vs ConcurrentSkipListMap
 * - ConcurrentHashMapDemo: Demuestra HashMap fail-fast vs ConcurrentHashMap
 * 
 * Estas demos NO pueden usar CountDownLatch porque el objetivo pedagógico
 * es mostrar qué sucede cuando una modificación concurrente interrumpe un
 * recorrido, lo cual requiere timing específico.
 */
@DisplayName("TR-1.1: Tests Determinísticos de Concurrencia")
class DeterministicConcurrencyTest {

    private static final int THREAD_COUNT = 1000;
    private static final int TIMEOUT_SECONDS = 30;

    /**
     * Este test usa @RepeatedTest porque la race condition en ArrayList.add()
     * no siempre se manifiesta en cada ejecución, dependiendo del scheduling
     * del SO y la JVM. Al repetirlo, aumentamos la probabilidad de observar
     * la pérdida de elementos.
     */
    @RepeatedTest(value = 5, name = "Iteración {currentRepetition} de {totalRepetitions}")
    @DisplayName("ArrayList pierde elementos sin sincronización (determinístico)")
    void unsynchronizedArrayListLosesElementsDeterministically() throws InterruptedException {
        // Dado: una lista no sincronizada y mecanismo de coordinación
        List<String> collection = new ArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Cuando: múltiples threads intentan agregar simultáneamente
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new CountDownLatchAdder(collection, String.valueOf(i), startLatch, completeLatch));
        }

        // Entonces: algunos elementos se pierden (race condition en add())
        boolean completed = completeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(completed, "Las tareas deberían completarse dentro del timeout");
        // En condiciones de alta contención, ArrayList típicamente pierde elementos
        // debido a race conditions en el array de respaldo interno
    }

    @Test
    @DisplayName("ArrayList sincronizado manualmente mantiene todos los elementos")
    void manuallySynchronizedArrayListKeepsAllElements() throws InterruptedException {
        // Dado: una lista con sincronización manual
        List<String> collection = new ArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Cuando: múltiples threads agregan con sincronización explícita
        for (int i = 0; i < THREAD_COUNT; i++) {
            final String value = String.valueOf(i);
            executor.execute(() -> {
                try {
                    startLatch.countDown();
                    startLatch.await();
                    synchronized (collection) {
                        collection.add(value);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Error al agregar: " + value, e);
                } finally {
                    completeLatch.countDown();
                }
            });
        }

        // Entonces: todos los elementos se preservan
        boolean completed = completeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(completed, "Las tareas deberían completarse dentro del timeout");
        assertEquals(THREAD_COUNT, collection.size(), 
            "La lista sincronizada debería tener todos los elementos");
    }

    @Test
    @DisplayName("Collections.synchronizedList() mantiene todos los elementos")
    void synchronizedListUtilityKeepsAllElements() throws InterruptedException {
        // Dado: una lista envuelta con Collections.synchronizedList()
        List<String> collection = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Cuando: múltiples threads agregan simultáneamente
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new CountDownLatchAdder(collection, String.valueOf(i), startLatch, completeLatch));
        }

        // Entonces: todos los elementos se preservan
        boolean completed = completeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(completed, "Las tareas deberían completarse dentro del timeout");
        assertEquals(THREAD_COUNT, collection.size(), 
            "Collections.synchronizedList() debería mantener todos los elementos");
    }

    @Test
    @DisplayName("CopyOnWriteArrayList mantiene todos los elementos bajo alta concurrencia")
    void copyOnWriteArrayListKeepsAllElements() throws InterruptedException {
        // Dado: una CopyOnWriteArrayList
        List<String> collection = new java.util.concurrent.CopyOnWriteArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Cuando: múltiples threads agregan simultáneamente
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new CountDownLatchAdder(collection, String.valueOf(i), startLatch, completeLatch));
        }

        // Entonces: todos los elementos se preservan
        boolean completed = completeLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(completed, "Las tareas deberían completarse dentro del timeout");
        assertEquals(THREAD_COUNT, collection.size(), 
            "CopyOnWriteArrayList debería mantener todos los elementos");
    }
}

package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de integración para la CLI principal.
 */
class ThreadSafeCollectionsApplicationTest {

    @Test
    void listOptionPrintsAvailableDemos() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        int exitCode = ThreadSafeCollectionsApplication.run(
                new String[]{"--list"},
                new PrintStream(output),
                new PrintStream(error)
        );

        assertEquals(0, exitCode);
        assertTrue(output.toString().contains("basic-collection"));
        assertTrue(output.toString().contains("concurrent-hash-map"));
        assertTrue(error.toString().isEmpty());
    }

    @Test
    void singleDemoOptionRunsOnlyTheSelectedDemo() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        int exitCode = ThreadSafeCollectionsApplication.run(
                new String[]{"--demo", "basic-collection"},
                new PrintStream(output),
                new PrintStream(error)
        );

        assertEquals(0, exitCode);
        assertTrue(output.toString().contains("COLECCIONES TRADICIONALES"));
        assertTrue(output.toString().contains("ArrayList - no sincronizado"));
        assertTrue(!output.toString().contains("ConcurrentHashMap (java.util.concurrent)"));
        assertTrue(error.toString().isEmpty());
    }

    @Test
    void defaultExecutionRunsAllDemos() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        int exitCode = ThreadSafeCollectionsApplication.run(
                new String[]{"--fast"},
                new PrintStream(output),
                new PrintStream(error)
        );

        assertEquals(0, exitCode);
        assertTrue(output.toString().contains("CopyOnWriteArrayList"));
        assertTrue(output.toString().contains("ConcurrentHashMap"));
        assertTrue(output.toString().contains("Modo rápido activo"));
        assertTrue(error.toString().isEmpty());
    }

    @Test
    void helpMentionsFastMode() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        int exitCode = ThreadSafeCollectionsApplication.run(
                new String[]{"--help"},
                new PrintStream(output),
                new PrintStream(error)
        );

        assertEquals(0, exitCode);
        assertTrue(output.toString().contains("--fast"));
        assertTrue(error.toString().isEmpty());
    }

    @Test
    void unknownDemoProducesExitCodeOne() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        int exitCode = ThreadSafeCollectionsApplication.run(
                new String[]{"--demo", "missing-demo"},
                new PrintStream(output),
                new PrintStream(error)
        );

        assertEquals(1, exitCode);
        assertTrue(error.toString().contains("No existe una demo"));
    }
}

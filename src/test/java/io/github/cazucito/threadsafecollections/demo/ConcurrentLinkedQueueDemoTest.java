package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ConcurrentLinkedQueueDemo.
 */
@DisplayName("ConcurrentLinkedQueueDemo Tests")
class ConcurrentLinkedQueueDemoTest {

    private final ConcurrentLinkedQueueDemo demo = new ConcurrentLinkedQueueDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("concurrent-linked-queue", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().toLowerCase().contains("concurrent"));
    }

    @Test
    @DisplayName("Demo has learning objective")
    void demoHasLearningObjective() {
        assertNotNull(demo.learningObjective());
        assertFalse(demo.learningObjective().isEmpty());
    }

    @Test
    @DisplayName("Demo has expected observation")
    void demoHasExpectedObservation() {
        assertNotNull(demo.expectedObservation());
        assertFalse(demo.expectedObservation().isEmpty());
    }

    @Test
    @DisplayName("Demo has key takeaway")
    void demoHasKeyTakeaway() {
        assertNotNull(demo.keyTakeaway());
        assertFalse(demo.keyTakeaway().isEmpty());
    }

    @Test
    @DisplayName("Run returns valid DemoResult")
    void runReturnsValidDemoResult() {
        DemoResult result = demo.run();

        assertNotNull(result);
        assertEquals("concurrent-linked-queue", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }

    @Test
    @DisplayName("Run returns result with constructor examples")
    void runReturnsResultWithConstructorExamples() {
        DemoResult result = demo.run();

        boolean hasConstructors = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("constructores") ||
                          m.text().toLowerCase().contains("vacío"));

        assertTrue(hasConstructors, "Debe contener información sobre constructores");
    }

    @Test
    @DisplayName("Demo mentions ConcurrentModificationException")
    void demoMentionsConcurrentModificationException() {
        DemoResult result = demo.run();

        boolean mentionsException = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("concurrentmodificationexception") ||
                          m.text().toLowerCase().contains("excepción"));

        assertTrue(mentionsException, "Debe mencionar ConcurrentModificationException");
    }

    @Test
    @DisplayName("Demo mentions non-blocking behavior")
    void demoMentionsNonBlockingBehavior() {
        DemoResult result = demo.run();

        boolean mentionsNonBlocking = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("no-bloqueante") ||
                          m.text().toLowerCase().contains("lock-free") ||
                          m.text().toLowerCase().contains("poll"));

        assertTrue(mentionsNonBlocking, "Debe mencionar comportamiento no-bloqueante");
    }
}

package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ArrayBlockingQueueDemo.
 */
@DisplayName("ArrayBlockingQueueDemo Tests")
class ArrayBlockingQueueDemoTest {

    private final ArrayBlockingQueueDemo demo = new ArrayBlockingQueueDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("array-blocking-queue", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().toLowerCase().contains("blocking"));
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
        assertEquals("array-blocking-queue", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }

    @Test
    @DisplayName("Run returns result with producer/consumer pattern")
    void runReturnsResultWithProducerConsumerPattern() {
        DemoResult result = demo.run();

        boolean hasProducerConsumer = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("productor") ||
                          m.text().toLowerCase().contains("consumidor") ||
                          m.text().toLowerCase().contains("producer") ||
                          m.text().toLowerCase().contains("consumer"));

        assertTrue(hasProducerConsumer, "Debe contener patrón productor/consumidor");
    }

    @Test
    @DisplayName("Demo mentions back-pressure")
    void demoMentionsBackPressure() {
        DemoResult result = demo.run();

        boolean mentionsBackPressure = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("back-pressure") ||
                          m.text().toLowerCase().contains("presión"));

        assertTrue(mentionsBackPressure, "Debe mencionar back-pressure");
    }

    @Test
    @DisplayName("Demo mentions timeout handling")
    void demoMentionsTimeoutHandling() {
        DemoResult result = demo.run();

        boolean mentionsTimeout = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("timeout") ||
                          m.text().toLowerCase().contains("offer") ||
                          m.text().toLowerCase().contains("poll"));

        assertTrue(mentionsTimeout, "Debe mencionar manejo de timeout");
    }
}

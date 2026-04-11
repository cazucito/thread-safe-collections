package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para ConcurrentSkipListSetDemo.
 */
@DisplayName("ConcurrentSkipListSetDemo Tests")
class ConcurrentSkipListSetDemoTest {

    private final ConcurrentSkipListSetDemo demo = new ConcurrentSkipListSetDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("concurrent-skip-list-set", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().toLowerCase().contains("skip"));
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
        assertEquals("concurrent-skip-list-set", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }
}

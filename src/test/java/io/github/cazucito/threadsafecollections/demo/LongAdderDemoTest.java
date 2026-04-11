package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para LongAdderDemo.
 */
@DisplayName("LongAdderDemo Tests")
class LongAdderDemoTest {

    private final LongAdderDemo demo = new LongAdderDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("long-adder", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().toLowerCase().contains("longadder") ||
                   demo.title().toLowerCase().contains("adder"));
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
        assertEquals("long-adder", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }

    @Test
    @DisplayName("Run returns result comparing AtomicLong vs LongAdder")
    void runReturnsResultWithComparison() {
        DemoResult result = demo.run();

        boolean hasAtomicLong = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("atomiclong"));

        boolean hasLongAdder = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("longadder"));

        assertTrue(hasAtomicLong, "Debe mencionar AtomicLong");
        assertTrue(hasLongAdder, "Debe mencionar LongAdder");
    }

    @Test
    @DisplayName("Demo mentions ConcurrentHashMap usage")
    void demoMentionsConcurrentHashMapUsage() {
        DemoResult result = demo.run();

        boolean mentionsHashMap = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("concurrenthashmap") ||
                          m.text().toLowerCase().contains("frecuencia"));

        assertTrue(mentionsHashMap, "Debe mencionar ConcurrentHashMap para frecuencias");
    }

    @Test
    @DisplayName("Demo explains when to use each counter")
    void demoExplainsWhenToUse() {
        DemoResult result = demo.run();

        boolean explainsUsage = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("cuándo usar") ||
                          m.text().toLowerCase().contains("cuando usar") ||
                          m.text().toLowerCase().contains("usa atomiclong") ||
                          m.text().toLowerCase().contains("usa longadder"));

        assertTrue(explainsUsage, "Debe explicar cuándo usar cada contador");
    }
}

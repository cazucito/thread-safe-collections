package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para CopyOnWriteArrayListDemo.
 */
@DisplayName("CopyOnWriteArrayListDemo Tests")
class CopyOnWriteArrayListDemoTest {

    private final CopyOnWriteArrayListDemo demo = new CopyOnWriteArrayListDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("copy-on-write-array-list", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().toLowerCase().contains("copy"));
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
        assertEquals("copy-on-write-array-list", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }

    @Test
    @DisplayName("Run returns result showing array list behavior")
    void runReturnsResultShowingArrayListBehavior() {
        DemoResult result = demo.run();

        // Debe mostrar información sobre ArrayList
        boolean hasArrayListInfo = result.messages().stream()
            .anyMatch(m -> m.text().toLowerCase().contains("arraylist") ||
                          m.text().toLowerCase().contains("copy"));

        assertTrue(hasArrayListInfo, "Debe contener información sobre ArrayList");
    }
}

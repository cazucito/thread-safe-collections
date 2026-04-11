package io.github.cazucito.threadsafecollections.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para BasicCollectionDemo.
 */
@DisplayName("BasicCollectionDemo Tests")
class BasicCollectionDemoTest {

    private final BasicCollectionDemo demo = new BasicCollectionDemo();

    @Test
    @DisplayName("Demo has correct id")
    void demoHasCorrectId() {
        assertEquals("basic-collection", demo.id());
    }

    @Test
    @DisplayName("Demo has non-empty title")
    void demoHasNonEmptyTitle() {
        assertNotNull(demo.title());
        assertFalse(demo.title().isEmpty());
        assertTrue(demo.title().contains("COLECCIONES") || demo.title().contains("Collection"));
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
        assertEquals("basic-collection", result.demoId());
        assertNotNull(result.title());
        assertFalse(result.messages().isEmpty(), "Debe tener al menos un mensaje");
    }

    @Test
    @DisplayName("Run returns result with expected structure")
    void runReturnsResultWithExpectedStructure() {
        DemoResult result = demo.run();

        // Debe tener mensajes de subtítulo para las diferentes colecciones
        boolean hasSubtitle = result.messages().stream()
            .anyMatch(m -> m.text().contains("ArrayList"));

        assertTrue(hasSubtitle, "Debe contener mensajes sobre ArrayList");
    }
}

package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de humo para validar que las demos registradas están listas para estudio.
 */
class DemoRegistrySmokeTest {

    @AfterEach
    void resetDelayMultiplier() {
        ThreadPause.setDelayMultiplier(1.0d);
    }

    @Test
    void everyRegisteredDemoHasPedagogicalMetadataAndOutput() {
        ThreadPause.setDelayMultiplier(0.25d);

        for (Demo demo : DemoRegistry.defaultRegistry().demos()) {
            assertFalse(demo.learningObjective().isBlank());
            assertFalse(demo.expectedObservation().isBlank());
            assertFalse(demo.keyTakeaway().isBlank());

            DemoResult result = demo.run();

            assertEquals(demo.id(), result.demoId());
            assertEquals(demo.title(), result.title());
            assertFalse(result.messages().isEmpty());
        }
    }

    @Test
    void findByPartialIdReturnsMatchingDemos() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        // "copy" debería encontrar copy-on-write-array-list y copy-on-write-array-set
        List<Demo> copyMatches = registry.findByPartialId("copy");
        assertEquals(2, copyMatches.size());
        assertTrue(copyMatches.stream().anyMatch(d -> d.id().equals("copy-on-write-array-list")));
        assertTrue(copyMatches.stream().anyMatch(d -> d.id().equals("copy-on-write-array-set")));
    }

    @Test
    void findByPartialIdIsCaseInsensitive() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        List<Demo> upperCase = registry.findByPartialId("COPY");
        List<Demo> lowerCase = registry.findByPartialId("copy");

        assertEquals(upperCase.size(), lowerCase.size());
    }

    @Test
    void findUniqueMatchReturnsSingleMatch() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        // "basic-collection" es único
        Optional<Demo> match = registry.findUniqueMatch("basic");
        assertTrue(match.isPresent());
        assertEquals("basic-collection", match.get().id());
    }

    @Test
    void findUniqueMatchReturnsEmptyForMultipleMatches() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        // "concurrent" coincide con múltiples demos
        Optional<Demo> match = registry.findUniqueMatch("concurrent");
        assertFalse(match.isPresent());
    }

    @Test
    void findUniqueMatchReturnsEmptyForNoMatches() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        Optional<Demo> match = registry.findUniqueMatch("nonexistent");
        assertFalse(match.isPresent());
    }

    @Test
    void fuzzySearchIncludesTitleMatches() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        // Buscar "ConcurrentHashMap" (aparece en título)
        List<Demo> matches = registry.fuzzySearch("concurrenthashmap");
        assertTrue(matches.stream().anyMatch(d -> d.id().equals("concurrent-hash-map")));
    }

    @Test
    void getSuggestionsTextContainsAllMatches() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        String suggestions = registry.getSuggestionsText("copy");
        assertTrue(suggestions.contains("copy-on-write-array-list"));
        assertTrue(suggestions.contains("copy-on-write-array-set"));
        assertTrue(suggestions.contains("Use el id completo"));
    }

    @Test
    void getSuggestionsTextForNoMatches() {
        DemoRegistry registry = DemoRegistry.defaultRegistry();

        String suggestions = registry.getSuggestionsText("xyz123");
        assertTrue(suggestions.contains("No se encontraron demos"));
    }
}

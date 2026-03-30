package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
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
}

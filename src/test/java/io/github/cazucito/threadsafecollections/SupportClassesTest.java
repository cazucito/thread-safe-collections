package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.ThreadPause;
import org.junit.jupiter.api.Test;

/**
 * Pruebas básicas para validar el soporte mínimo del proyecto.
 */
class SupportClassesTest {

    @Test
    void debugFlagCanBeToggled() {
        ConsolePrinter.disableDebug();
        assertFalse(ConsolePrinter.isDebugEnabled());

        ConsolePrinter.enableDebug();
        assertTrue(ConsolePrinter.isDebugEnabled());
    }

    @Test
    void sleepHelpersDoNotThrow() {
        assertDoesNotThrow(() -> ThreadPause.sleepMillis(1));
        assertDoesNotThrow(() -> ThreadPause.sleepBetween(1, 2));
    }
}

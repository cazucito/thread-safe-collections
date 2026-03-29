package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;
import io.github.cazucito.threadsafecollections.support.ThreadPause;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * Pruebas básicas para utilerías de soporte.
 */
class SupportClassesTest {

    @Test
    void debugMessagesAreSuppressedWhenDebugIsDisabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsolePrinter printer = new ConsolePrinter(new PrintStream(output), false);

        printer.print(MessageType.DEBUG, "mensaje oculto");

        assertTrue(output.toString().isEmpty());
    }

    @Test
    void regularMessagesArePrintedEvenWhenDebugIsDisabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsolePrinter printer = new ConsolePrinter(new PrintStream(output), false);

        printer.print(MessageType.MESSAGE, "mensaje visible");

        assertFalse(output.toString().isEmpty());
    }

    @Test
    void sleepHelpersDoNotThrow() {
        assertDoesNotThrow(() -> ThreadPause.sleepMillis(1));
        assertDoesNotThrow(() -> ThreadPause.sleepBetween(1, 2));
    }
}

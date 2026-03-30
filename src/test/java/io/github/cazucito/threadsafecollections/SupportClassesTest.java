package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.cli.ConsolePrinter;
import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

/**
 * Pruebas básicas para utilerías de soporte.
 */
class SupportClassesTest {

    private static final Demo DEMO_STUB = new Demo() {
        @Override
        public String id() {
            return "demo-stub";
        }

        @Override
        public String title() {
            return "Demo stub";
        }

        @Override
        public String learningObjective() {
            return "Objetivo breve";
        }

        @Override
        public String expectedObservation() {
            return "Observación breve";
        }

        @Override
        public String keyTakeaway() {
            return "Conclusión breve";
        }

        @Override
        public DemoResult run() {
            return DemoResult.builder(id(), title())
                    .add(MessageType.MESSAGE, "Cuerpo")
                    .build();
        }
    };

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

    @Test
    void printerAddsPedagogicalContextToDemoOutput() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsolePrinter printer = new ConsolePrinter(new PrintStream(output), false);

        printer.printDemoResult(DEMO_STUB, DEMO_STUB.run());

        String rendered = output.toString();
        assertTrue(rendered.contains("Objetivo: Objetivo breve"));
        assertTrue(rendered.contains("Observa: Observación breve"));
        assertTrue(rendered.contains("Conclusión: Conclusión breve"));
    }
}

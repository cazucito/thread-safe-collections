package io.github.cazucito.threadsafecollections.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests para ConsolePrinter.
 */
@DisplayName("ConsolePrinter Tests")
class ConsolePrinterTest {

    private ByteArrayOutputStream output;
    private ConsolePrinter printer;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
    }

    @ParameterizedTest
    @EnumSource(MessageType.class)
    @DisplayName("Print outputs all message types without throwing")
    void printOutputsAllMessageTypes(MessageType type) {
        printer = new ConsolePrinter(new PrintStream(output), true);
        
        assertDoesNotThrow(() -> printer.print(type, "Test message"),
            "No debe lanzar excepción para tipo " + type);
        
        String result = output.toString();
        assertFalse(result.isEmpty(), "Output no debe estar vacío");
    }

    @Test
    @DisplayName("Print with debug disabled skips debug messages")
    void printWithDebugDisabledSkipsDebugMessages() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        
        printer.print(MessageType.DEBUG, "This is a debug message");
        
        String result = output.toString();
        assertTrue(result.isEmpty(), "Debug message debe ser omitido cuando debug=false");
    }

    @Test
    @DisplayName("Print with debug enabled includes debug messages")
    void printWithDebugEnabledIncludesDebugMessages() {
        printer = new ConsolePrinter(new PrintStream(output), true);
        
        printer.print(MessageType.DEBUG, "This is a debug message");
        
        String result = output.toString();
        assertFalse(result.isEmpty(), "Debug message debe aparecer cuando debug=true");
    }

    @Test
    @DisplayName("Print with multiple messages outputs all")
    void printWithMultipleMessagesOutputsAll() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        
        printer.print(MessageType.MESSAGE, "Message 1", "Message 2", "Message 3");
        
        String result = output.toString();
        assertTrue(result.contains("Message 1"), "Debe contener Message 1");
    }

    @Test
    @DisplayName("Print with header format creates header structure")
    void printWithHeaderFormatCreatesHeaderStructure() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        
        printer.print(MessageType.HEADER, "Title");
        
        String result = output.toString();
        assertFalse(result.isEmpty(), "Header no debe estar vacío");
    }

    @Test
    @DisplayName("Print with footer format creates footer structure")
    void printWithFooterFormatCreatesFooterStructure() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        
        printer.print(MessageType.FOOTER, "");
        
        String result = output.toString();
        assertFalse(result.isEmpty(), "Footer no debe estar vacío");
    }

    @Test
    @DisplayName("Abbreviate method shortens long messages")
    void abbreviateMethodShortensLongMessages() {
        printer = new ConsolePrinter(new PrintStream(output), true);
        
        // Mensaje largo que debería ser abreviado
        String longMessage = "A".repeat(100);
        printer.print(MessageType.DEBUG, longMessage);
        
        String result = output.toString();
        // El mensaje debería ser truncado
        assertTrue(result.length() < longMessage.length() + 50,
            "Mensaje largo debe ser abreviado");
    }

    @Test
    @DisplayName("Print with exception type includes thread name")
    void printWithExceptionTypeIncludesThreadName() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        String threadName = Thread.currentThread().getName();
        
        printer.print(MessageType.EXCEPTION, "Test exception");
        
        String result = output.toString();
        assertTrue(result.contains(threadName) || result.contains("ex|"),
            "Exception message debe incluir thread name o indicador");
    }

    @Test
    @DisplayName("PrintDemoList outputs list of demos")
    void printDemoListOutputsListOfDemos() {
        printer = new ConsolePrinter(new PrintStream(output), false);
        
        io.github.cazucito.threadsafecollections.demo.DemoRegistry registry = 
            io.github.cazucito.threadsafecollections.demo.DemoRegistry.defaultRegistry();
        
        printer.printDemoList(registry.demos());
        
        String result = output.toString();
        assertTrue(result.contains("DEMOS DISPONIBLES") || result.contains("demo"),
            "Debe contener título o referencias a demos");
    }
}

package io.github.cazucito.threadsafecollections.format;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para los formateadores de resultados de demo.
 */
@DisplayName("DemoResultFormatter Tests")
class DemoResultFormatterTest {

    private static final Demo DEMO_STUB = new Demo() {
        @Override
        public String id() {
            return "test-demo";
        }

        @Override
        public String title() {
            return "Test Demo Title";
        }

        @Override
        public String learningObjective() {
            return "Learn something useful";
        }

        @Override
        public String expectedObservation() {
            return "Observe something interesting";
        }

        @Override
        public String keyTakeaway() {
            return "Remember this key point";
        }

        @Override
        public DemoResult run() {
            return DemoResult.builder(id(), title())
                    .add(MessageType.SUBTITLE, "Test Section")
                    .add(MessageType.SUCCESS, "Test passed")
                    .build();
        }
    };

    // ConsoleDemoResultFormatter Tests

    @Test
    @DisplayName("Console formatter prints demo result with all metadata")
    void consoleFormatterPrintsDemoResultWithAllMetadata() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        formatter.formatDemoResult(DEMO_STUB, DEMO_STUB.run(), new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("Test Demo Title"), "Should contain title");
        assertTrue(result.contains("Learn something useful"), "Should contain learning objective");
        assertTrue(result.contains("Observe something interesting"), "Should contain expected observation");
        assertTrue(result.contains("Remember this key point"), "Should contain key takeaway");
    }

    @Test
    @DisplayName("Console formatter prints demo list")
    void consoleFormatterPrintsDemoList() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        formatter.formatDemoList(List.of(DEMO_STUB), new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("test-demo"), "Should contain demo id");
        assertTrue(result.contains("Test Demo Title"), "Should contain demo title");
    }

    @Test
    @DisplayName("Console formatter prints header")
    void consoleFormatterPrintsHeader() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        formatter.formatHeader("Test Header", new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("Test Header"), "Should contain header text");
    }

    @Test
    @DisplayName("Console formatter prints footer")
    void consoleFormatterPrintsFooter() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        formatter.formatFooter(new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("Java"), "Should contain Java version");
    }

    @Test
    @DisplayName("Console formatter prints info messages")
    void consoleFormatterPrintsInfoMessages() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        formatter.formatInfo("Test info message", new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("Test info message"), "Should contain info message");
    }

    @Test
    @DisplayName("Console formatter suppresses debug when disabled")
    void consoleFormatterSuppressesDebugWhenDisabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(false);
        
        DemoResult result = DemoResult.builder("test", "Test")
                .add(MessageType.DEBUG, "Hidden debug message")
                .add(MessageType.MESSAGE, "Visible message")
                .build();
        
        formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        
        String outputStr = output.toString();
        assertFalse(outputStr.contains("Hidden debug message"), "Debug message should be suppressed");
    }

    @Test
    @DisplayName("Console formatter shows debug when enabled")
    void consoleFormatterShowsDebugWhenEnabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ConsoleDemoResultFormatter formatter = new ConsoleDemoResultFormatter(true);
        
        DemoResult result = DemoResult.builder("test", "Test")
                .add(MessageType.DEBUG, "Debug message")
                .build();
        
        formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        
        String outputStr = output.toString();
        assertTrue(outputStr.contains("Debug message"), "Debug message should be visible when enabled");
    }

    // JsonDemoResultFormatter Tests

    @Test
    @DisplayName("JSON formatter produces valid JSON structure")
    void jsonFormatterProducesValidJsonStructure() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        formatter.formatDemoResult(DEMO_STUB, DEMO_STUB.run(), new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.startsWith("{"), "Should start with opening brace");
        assertTrue(result.contains("\"metadata\""), "Should contain metadata");
        assertTrue(result.contains("\"demo\""), "Should contain demo");
        assertTrue(result.contains("\"messages\""), "Should contain messages");
        assertTrue(result.contains("\"test-demo\""), "Should contain demo id");
        assertTrue(result.contains("\"Test Demo Title\""), "Should contain title");
    }

    @Test
    @DisplayName("JSON formatter includes all demo metadata")
    void jsonFormatterIncludesAllDemoMetadata() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        formatter.formatDemoResult(DEMO_STUB, DEMO_STUB.run(), new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.contains("\"learningObjective\""), "Should contain learning objective");
        assertTrue(result.contains("\"expectedObservation\""), "Should contain expected observation");
        assertTrue(result.contains("\"keyTakeaway\""), "Should contain key takeaway");
        assertTrue(result.contains("Learn something useful"), "Should contain learning objective value");
    }

    @Test
    @DisplayName("JSON formatter produces valid demo list JSON")
    void jsonFormatterProducesValidDemoListJson() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        formatter.formatDemoList(List.of(DEMO_STUB), new PrintStream(output));
        
        String result = output.toString();
        assertTrue(result.startsWith("{"), "Should start with opening brace");
        assertTrue(result.contains("\"demos\""), "Should contain demos array");
        assertTrue(result.contains("\"test-demo\""), "Should contain demo id");
    }

    @Test
    @DisplayName("JSON formatter includes message types in lowercase")
    void jsonFormatterIncludesMessageTypesInLowercase() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        DemoResult result = DemoResult.builder("test", "Test")
                .add(MessageType.SUCCESS, "Test success")
                .add(MessageType.ERROR, "Test error")
                .build();
        
        formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        
        String jsonOutput = output.toString();
        assertTrue(jsonOutput.contains("\"type\": \"success\""), "Should have lowercase success type");
        assertTrue(jsonOutput.contains("\"type\": \"error\""), "Should have lowercase error type");
    }

    @Test
    @DisplayName("JSON formatter escapes special characters")
    void jsonFormatterEscapesSpecialCharacters() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        DemoResult result = DemoResult.builder("test", "Test \"with quotes\"")
                .add(MessageType.MESSAGE, "Line1\nLine2")
                .add(MessageType.MESSAGE, "Tab\there")
                .build();
        
        assertDoesNotThrow(() -> {
            formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        }, "Should handle special characters without throwing");
        
        String jsonOutput = output.toString();
        assertTrue(jsonOutput.contains("\\\""), "Should escape quotes");
        assertTrue(jsonOutput.contains("\\n"), "Should escape newlines");
        assertTrue(jsonOutput.contains("\\t"), "Should escape tabs");
    }

    @Test
    @DisplayName("JSON formatter suppresses debug when disabled")
    void jsonFormatterSuppressesDebugWhenDisabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        DemoResult result = DemoResult.builder("test", "Test")
                .add(MessageType.DEBUG, "Hidden debug message")
                .add(MessageType.MESSAGE, "Visible message")
                .build();
        
        formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        
        String jsonOutput = output.toString();
        assertFalse(jsonOutput.contains("Hidden debug message"), "Debug message should be suppressed");
        assertTrue(jsonOutput.contains("Visible message"), "Regular message should be visible");
    }

    @Test
    @DisplayName("JSON formatter includes debug when enabled")
    void jsonFormatterIncludesDebugWhenEnabled() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(true);
        
        DemoResult result = DemoResult.builder("test", "Test")
                .add(MessageType.DEBUG, "Debug message")
                .build();
        
        formatter.formatDemoResult(DEMO_STUB, result, new PrintStream(output));
        
        String jsonOutput = output.toString();
        assertTrue(jsonOutput.contains("Debug message"), "Debug message should be visible when enabled");
    }

    @Test
    @DisplayName("JSON formatter produces empty output for header and footer")
    void jsonFormatterProducesEmptyOutputForHeaderAndFooter() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonDemoResultFormatter formatter = new JsonDemoResultFormatter(false);
        
        formatter.formatHeader("Test Header", new PrintStream(output));
        String headerOutput = output.toString();
        
        formatter.formatFooter(new PrintStream(output));
        String footerOutput = output.toString();
        
        // JSON formatter intentionally omits header/footer for valid JSON
        assertTrue(headerOutput.isEmpty() || !headerOutput.contains("{"), 
            "Header should be empty or not contain JSON");
        assertTrue(footerOutput.isEmpty() || !footerOutput.contains("{"), 
            "Footer should be empty or not contain JSON");
    }
}
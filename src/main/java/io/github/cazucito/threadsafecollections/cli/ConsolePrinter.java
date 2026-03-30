package io.github.cazucito.threadsafecollections.cli;

import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoMessage;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Renderiza en consola la salida estructurada de la aplicación.
 */
public final class ConsolePrinter {

    private static final String HEADER_FORMAT = "||:::::::|%70s ||%n||:::::::|%70s ||%n||:::::::|%70s ||%n";
    private static final String FOOTER_FORMAT = "||:::::::|%70s ||%n||:::::::|%70s ||%n||:::::::|%70s ||%n";
    private static final String TITLE_FORMAT = "||:::::::| :::::::::::::::%54s ||%n";
    private static final String SUBTITLE_FORMAT = "         | ---------------%54s  |%n";
    private static final String MESSAGE_FORMAT = "         | %-70s |%n";
    private static final String INFO_FORMAT = "     info| %-70s  |%n";
    private static final String LOGIC_ERROR_FORMAT = "      bug| %-70s |%n";
    private static final String SUCCESS_FORMAT = "       ok| %-70s |%n";
    private static final String EXCEPTION_FORMAT = "       ex| %-70s |%n";
    private static final String ERROR_FORMAT = "    error| %-70s |%n";
    private static final String DEBUG_FORMAT = "    debug| %-70s |%n";

    private final PrintStream output;
    private final boolean debugEnabled;

    public ConsolePrinter(PrintStream output, boolean debugEnabled) {
        this.output = output;
        this.debugEnabled = debugEnabled;
    }

    /**
     * Imprime un mensaje arbitrario.
     *
     * @param messageType tipo de mensaje
     * @param messages    textos a imprimir
     */
    public void print(MessageType messageType, String... messages) {
        String format = "";
        List<String> values = new ArrayList<>(Arrays.asList(messages));

        switch (messageType) {
            case HEADER:
                values.add(0, "=====================================================================");
                values.add("---------------------------------------------------------------------");
                format = HEADER_FORMAT;
                break;
            case FOOTER:
                values.add("=====================================================================");
                values.add(0, "---------------------------------------------------------------------");
                values.set(1, "Java " + System.getProperty("java.version") + " | " + LocalDate.now().getYear());
                format = FOOTER_FORMAT;
                break;
            case TITLE:
                format = TITLE_FORMAT;
                break;
            case SUBTITLE:
                format = SUBTITLE_FORMAT;
                break;
            case MESSAGE:
                format = MESSAGE_FORMAT;
                break;
            case SUCCESS:
                format = SUCCESS_FORMAT;
                break;
            case INFO:
                format = INFO_FORMAT;
                break;
            case LOGIC_ERROR:
                values.set(0, withThreadName(values.get(0)));
                format = LOGIC_ERROR_FORMAT;
                break;
            case EXCEPTION:
                values.set(0, withThreadName(values.get(0)));
                format = EXCEPTION_FORMAT;
                break;
            case ERROR:
                values.set(0, withThreadName(values.get(0)));
                format = ERROR_FORMAT;
                break;
            case DEBUG:
                if (!debugEnabled) {
                    return;
                }
                values.set(0, abbreviate(withThreadName(values.get(0))));
                format = DEBUG_FORMAT;
                break;
            default:
                throw new IllegalArgumentException("Unsupported message type: " + messageType);
        }

        output.format(format, values.toArray());
    }

    /**
     * Imprime el resultado completo de una demo.
     *
     * @param result resultado estructurado de la demo
     */
    public void printDemoResult(Demo demo, DemoResult result) {
        print(MessageType.TITLE, result.title());
        print(MessageType.INFO, "Objetivo: " + demo.learningObjective());
        print(MessageType.INFO, "Observa: " + demo.expectedObservation());
        for (DemoMessage message : result.messages()) {
            print(message.type(), message.text());
        }
        print(MessageType.INFO, "Conclusión: " + demo.keyTakeaway());
    }

    /**
     * Imprime la lista de demos disponibles.
     *
     * @param demos demos registradas
     */
    public void printDemoList(List<Demo> demos) {
        print(MessageType.TITLE, "DEMOS DISPONIBLES");
        for (Demo demo : demos) {
            print(MessageType.MESSAGE, demo.id() + " - " + demo.title());
            print(MessageType.INFO, "Objetivo: " + demo.learningObjective());
        }
    }

    private String withThreadName(String message) {
        return Thread.currentThread().getName() + "> " + message;
    }

    private String abbreviate(String message) {
        if (message.length() <= 50) {
            return message;
        }

        return message.substring(0, 48) + " ...";
    }
}

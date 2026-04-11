package io.github.cazucito.threadsafecollections.format;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoMessage;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Formateador de resultados para salida en consola.
 * 
 * Implementa el formato visual original con bordes ASCII
 * y colores semánticos por tipo de mensaje.
 */
public final class ConsoleDemoResultFormatter implements DemoResultFormatter {

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

    private final boolean debugEnabled;

    /**
     * Crea un formateador de consola.
     *
     * @param debugEnabled si true, muestra mensajes de debug
     */
    public ConsoleDemoResultFormatter(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    @Override
    public void formatDemoResult(Demo demo, DemoResult result, PrintStream output) {
        formatMessage(MessageType.TITLE, output, result.title());
        formatMessage(MessageType.INFO, output, "Objetivo: " + demo.learningObjective());
        formatMessage(MessageType.INFO, output, "Observa: " + demo.expectedObservation());
        for (DemoMessage message : result.messages()) {
            formatMessage(message.type(), output, message.text());
        }
        formatMessage(MessageType.INFO, output, "Conclusión: " + demo.keyTakeaway());
    }

    @Override
    public void formatDemoList(List<Demo> demos, PrintStream output) {
        formatMessage(MessageType.TITLE, output, "DEMOS DISPONIBLES");
        for (Demo demo : demos) {
            formatMessage(MessageType.MESSAGE, output, demo.id() + " - " + demo.title());
            formatMessage(MessageType.INFO, output, "Objetivo: " + demo.learningObjective());
        }
    }

    @Override
    public void formatHeader(String title, PrintStream output) {
        formatMessage(MessageType.HEADER, output, title);
    }

    @Override
    public void formatFooter(PrintStream output) {
        formatMessage(MessageType.FOOTER, output, "");
    }

    @Override
    public void formatInfo(String message, PrintStream output) {
        formatMessage(MessageType.INFO, output, message);
    }

    private void formatMessage(MessageType messageType, PrintStream output, String... messages) {
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

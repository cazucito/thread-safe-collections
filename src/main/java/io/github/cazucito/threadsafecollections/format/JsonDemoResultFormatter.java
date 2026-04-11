package io.github.cazucito.threadsafecollections.format;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoMessage;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.PrintStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Formateador de resultados para salida en formato JSON.
 * 
 * Produce JSON estructurado con toda la información de las demos,
 * incluyendo metadatos, mensajes y estado de ejecución.
 */
public final class JsonDemoResultFormatter implements DemoResultFormatter {

    private final boolean debugEnabled;

    /**
     * Crea un formateador JSON.
     *
     * @param debugEnabled si true, incluye mensajes de debug
     */
    public JsonDemoResultFormatter(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    @Override
    public void formatDemoResult(Demo demo, DemoResult result, PrintStream output) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Metadata
        json.append("  \"metadata\": {\n");
        json.append("    \"timestamp\": \"").append(escapeJson(Instant.now().toString())).append("\",\n");
        json.append("    \"javaVersion\": \"").append(escapeJson(System.getProperty("java.version"))).append("\",\n");
        json.append("    \"format\": \"json\"\n");
        json.append("  },\n");
        
        // Demo info
        json.append("  \"demo\": {\n");
        json.append("    \"id\": \"").append(escapeJson(result.demoId())).append("\",\n");
        json.append("    \"title\": \"").append(escapeJson(result.title())).append("\",\n");
        json.append("    \"learningObjective\": \"").append(escapeJson(demo.learningObjective())).append("\",\n");
        json.append("    \"expectedObservation\": \"").append(escapeJson(demo.expectedObservation())).append("\",\n");
        json.append("    \"keyTakeaway\": \"").append(escapeJson(demo.keyTakeaway())).append("\"\n");
        json.append("  },\n");
        
        // Messages
        json.append("  \"messages\": [\n");
        List<DemoMessage> messages = result.messages();
        if (!debugEnabled) {
            messages = messages.stream()
                    .filter(m -> m.type() != MessageType.DEBUG)
                    .collect(Collectors.toList());
        }
        
        for (int i = 0; i < messages.size(); i++) {
            DemoMessage message = messages.get(i);
            json.append("    {\n");
            json.append("      \"type\": \"").append(message.type().name().toLowerCase()).append("\",\n");
            json.append("      \"text\": \"").append(escapeJson(message.text())).append("\"\n");
            json.append("    }");
            if (i < messages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        
        json.append("}");
        output.println(json.toString());
    }

    @Override
    public void formatDemoList(List<Demo> demos, PrintStream output) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"demos\": [\n");
        
        for (int i = 0; i < demos.size(); i++) {
            Demo demo = demos.get(i);
            json.append("    {\n");
            json.append("      \"id\": \"").append(escapeJson(demo.id())).append("\",\n");
            json.append("      \"title\": \"").append(escapeJson(demo.title())).append("\",\n");
            json.append("      \"learningObjective\": \"").append(escapeJson(demo.learningObjective())).append("\"\n");
            json.append("    }");
            if (i < demos.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}");
        output.println(json.toString());
    }

    @Override
    public void formatHeader(String title, PrintStream output) {
        // En modo JSON, el header se omite o se incluye en cada demo individual
        // No se imprime nada para mantener JSON válido
    }

    @Override
    public void formatFooter(PrintStream output) {
        // En modo JSON, el footer se omite para mantener JSON válido
    }

    @Override
    public void formatInfo(String message, PrintStream output) {
        // En modo JSON, los mensajes informativos se incluyen en la estructura
    }

    /**
     * Escapa caracteres especiales en strings JSON.
     *
     * @param input string a escapar
     * @return string escapado
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
            }
        }
        return result.toString();
    }
}

package io.github.cazucito.threadsafecollections.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Generador interactivo de nuevas demos.
 *
 * Uso:
 * ./mvnw compile exec:java -Dexec.mainClass="io.github.cazucito.threadsafecollections.cli.NewDemoGenerator"
 *
 * O con perfil (recomendado):
 * ./mvnw compile exec:java -Pnew-demo
 */
public final class NewDemoGenerator {

    private static final String DEMO_PACKAGE = "io.github.cazucito.threadsafecollections.demo";

    private NewDemoGenerator() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsolePrinter printer = new ConsolePrinter(System.out, false);

        printer.print(MessageType.TITLE, "GENERADOR DE NUEVA DEMO");

        printer.print(MessageType.INFO, "Ingresa los datos de la nueva demo:");
        System.out.println();

        String demoId = prompt(scanner, "ID (kebab-case, ej: queue-demo): ");
        if (!demoId.matches("^[a-z][a-z0-9-]*$")) {
            printer.print(MessageType.ERROR, "ID inválido. Usa solo minúsculas, números y guiones.");
            System.exit(1);
        }

        String title = prompt(scanner, "Título (ej: ConcurrentLinkedQueue): ");
        String learningObjective = prompt(scanner, "Objetivo de aprendizaje: ");
        String expectedObservation = prompt(scanner, "Qué observar: ");
        String keyTakeaway = prompt(scanner, "Conclusión clave: ");

        String className = toClassName(demoId);

        try {
            Path demoPath = generateDemoClass(demoId, className, title, learningObjective,
                expectedObservation, keyTakeaway);
            Path testPath = generateTestClass(demoId, className, title);

            printer.print(MessageType.SUCCESS, "✓ Demo generada: " + demoPath);
            printer.print(MessageType.SUCCESS, "✓ Test generado: " + testPath);

            printer.print(MessageType.INFO, "");
            printer.print(MessageType.INFO, "Pasos manuales:");
            printer.print(MessageType.MESSAGE, "1. Registra la demo en DemoRegistry.defaultRegistry()");
            printer.print(MessageType.MESSAGE, "2. Implementa el método run() en " + className);
            printer.print(MessageType.MESSAGE, "3. Ejecuta: ./mvnw compile");

        } catch (IOException e) {
            printer.print(MessageType.ERROR, "Error generando archivos: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String prompt(Scanner scanner, String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Campo requerido.");
            return prompt(scanner, message);
        }
        return input;
    }

    private static String toClassName(String kebabCase) {
        StringBuilder result = new StringBuilder();
        boolean capitalize = true;

        for (char c : kebabCase.toCharArray()) {
            if (c == '-') {
                capitalize = true;
            } else if (capitalize) {
                result.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                result.append(c);
            }
        }

        result.append("Demo");
        return result.toString();
    }

    private static Path generateDemoClass(String demoId, String className, String title,
                                          String learningObjective, String expectedObservation,
                                          String keyTakeaway) throws IOException {

        String content = String.format(
            "package io.github.cazucito.threadsafecollections.demo;\n\n" +
            "import io.github.cazucito.threadsafecollections.cli.MessageType;\n\n" +
            "/**\n" +
            " * Demo: %s\n" +
            " *\n" +
            " * Created: %s\n" +
            " */\n" +
            "public final class %s implements Demo {\n\n" +
            "    @Override\n" +
            "    public String id() {\n" +
            "        return \"%s\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public String title() {\n" +
            "        return \"%s\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public String learningObjective() {\n" +
            "        return \"%s\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public String expectedObservation() {\n" +
            "        return \"%s\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public String keyTakeaway() {\n" +
            "        return \"%s\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public DemoResult run() {\n" +
            "        DemoResult.Builder result = DemoResult.builder(id(), title());\n\n" +
            "        // TODO: Implementa la lógica de la demo aquí\n" +
            "        result.add(MessageType.SUBTITLE, \"IMPLEMENTACIÓN PENDIENTE\");\n" +
            "        result.add(MessageType.MESSAGE, \"Edita %s.java para completar esta demo\");\n\n" +
            "        return result.build();\n" +
            "    }\n" +
            "}\n",
            title,
            LocalDate.now().format(DateTimeFormatter.ISO_DATE),
            className,
            demoId,
            title,
            learningObjective,
            expectedObservation,
            keyTakeaway,
            className
        );

        Path sourceDir = Paths.get("src/main/java/io/github/cazucito/threadsafecollections/demo");
        Files.createDirectories(sourceDir);
        Path filePath = sourceDir.resolve(className + ".java");
        Files.writeString(filePath, content);

        return filePath;
    }

    private static Path generateTestClass(String demoId, String className, String title) throws IOException {
        String content = String.format(
            "package io.github.cazucito.threadsafecollections;\n\n" +
            "import static org.junit.jupiter.api.Assertions.*;\n\n" +
            "import io.github.cazucito.threadsafecollections.demo.%s;\n" +
            "import org.junit.jupiter.api.DisplayName;\n" +
            "import org.junit.jupiter.api.Test;\n\n" +
            "/**\n" +
            " * Tests para %s\n" +
            " */\n" +
            "@DisplayName(\"%s Tests\")\n" +
            "class %sTest {\n\n" +
            "    @Test\n" +
            "    @DisplayName(\"Demo tiene metadata válida\")\n" +
            "    void demoHasValidMetadata() {\n" +
            "        %s demo = new %s();\n\n" +
            "        assertEquals(\"%s\", demo.id());\n" +
            "        assertNotNull(demo.title());\n" +
            "        assertFalse(demo.title().isEmpty());\n" +
            "        assertNotNull(demo.learningObjective());\n" +
            "        assertNotNull(demo.expectedObservation());\n" +
            "        assertNotNull(demo.keyTakeaway());\n" +
            "    }\n" +
            "}\n",
            className, className, className, className, className, className, demoId
        );

        Path testDir = Paths.get("src/test/java/io/github/cazucito/threadsafecollections");
        Files.createDirectories(testDir);
        Path filePath = testDir.resolve(className + "Test.java");
        Files.writeString(filePath, content);

        return filePath;
    }
}
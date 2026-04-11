package io.github.cazucito.threadsafecollections.cli;

/**
 * Validador de entorno para el proyecto.
 * 
 * Verifica que el entorno cumple con los requisitos mínimos:
 * - Java 17 o superior
 * - Maven disponible
 * 
 * Esta clase puede ejecutarse standalone via Maven:
 * ./mvnw compile exec:java -Dexec.mainClass="...EnvironmentValidator"
 */
public final class EnvironmentValidator {

    private static final int MIN_JAVA_VERSION = 17;
    private static final String MIN_MAVEN_VERSION = "3.9";

    private EnvironmentValidator() {
    }

    /**
     * Punto de entrada para validación de entorno.
     *
     * @param args argumentos de línea de comandos (ignorados)
     */
    public static void main(String[] args) {
        int exitCode = validate();
        System.exit(exitCode);
    }

    /**
     * Valida el entorno y retorna código de salida.
     *
     * @return 0 si el entorno es válido, 1 si no lo es
     */
    public static int validate() {
        ConsolePrinter printer = new ConsolePrinter(System.out, false);
        
        printer.print(MessageType.TITLE, "VALIDACIÓN DE ENTORNO");
        
        boolean javaOk = checkJava(printer);
        boolean mavenOk = checkMaven(printer);
        
        if (javaOk && mavenOk) {
            printer.print(MessageType.SUCCESS, "✓ Entorno listo para usar thread-safe-collections");
            printer.print(MessageType.INFO, "Puedes ejecutar: ./mvnw test");
            return 0;
        }
        
        printer.print(MessageType.ERROR, "✗ Entorno no cumple requisitos mínimos");
        
        if (!javaOk) {
            printJavaHelp(printer);
        }
        
        return 1;
    }

    private static boolean checkJava(ConsolePrinter printer) {
        String javaVersion = System.getProperty("java.version");
        printer.print(MessageType.INFO, "Java version: " + javaVersion);
        
        int majorVersion = parseMajorVersion(javaVersion);
        
        if (majorVersion >= MIN_JAVA_VERSION) {
            printer.print(MessageType.SUCCESS, "✓ Java " + majorVersion + " (requerido: " + MIN_JAVA_VERSION + ")");
            return true;
        }
        
        printer.print(MessageType.ERROR, "✗ Java " + majorVersion + " (requerido: " + MIN_JAVA_VERSION + "+)");
        return false;
    }

    private static boolean checkMaven(ConsolePrinter printer) {
        String mavenVersion = System.getProperty("maven.version");
        
        if (mavenVersion != null && !mavenVersion.isEmpty()) {
            printer.print(MessageType.INFO, "Maven version: " + mavenVersion);
            printer.print(MessageType.SUCCESS, "✓ Maven disponible");
            return true;
        }
        
        // Verificar si estamos ejecutando via Maven Wrapper
        String wrapper = System.getenv("MVNW_VERBOSE");
        if (wrapper != null || System.getProperty("maven.home") != null) {
            printer.print(MessageType.INFO, "Maven: usando Maven Wrapper");
            printer.print(MessageType.SUCCESS, "✓ Maven Wrapper disponible");
            return true;
        }
        
        printer.print(MessageType.ERROR, "✗ Maven no detectado");
        return false;
    }

    private static int parseMajorVersion(String version) {
        if (version.startsWith("1.")) {
            // Java 8 y anteriores: 1.8.0_xxx
            String[] parts = version.split("\\.");
            if (parts.length >= 2) {
                return Integer.parseInt(parts[1]);
            }
        }
        
        // Java 9+: 11.0.1, 17.0.5, 21.0.1
        String[] parts = version.split("\\.|\\-");
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void printJavaHelp(ConsolePrinter printer) {
        printer.print(MessageType.SUBTITLE, "INSTALACIÓN DE JAVA 17+");
        printer.print(MessageType.MESSAGE, "Opción 1: SDKMAN (recomendado)");
        printer.print(MessageType.MESSAGE, "  curl -s \"https://get.sdkman.io\" | bash");
        printer.print(MessageType.MESSAGE, "  source \"$HOME/.sdkman/bin/sdkman-init.sh\"");
        printer.print(MessageType.MESSAGE, "  sdk install java 17.0.13-tem");
        printer.print(MessageType.MESSAGE, "");
        printer.print(MessageType.MESSAGE, "Opción 2: Descarga manual");
        printer.print(MessageType.MESSAGE, "  https://adoptium.net/temurin/releases/?version=17");
        printer.print(MessageType.MESSAGE, "");
        printer.print(MessageType.MESSAGE, "Windows: usar el instalador de Eclipse Temurin");
        printer.print(MessageType.MESSAGE, "macOS: brew install temurin17");
    }
}

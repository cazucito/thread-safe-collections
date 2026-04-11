package io.github.cazucito.threadsafecollections;

import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.cli.ConsolePrinter;
import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import io.github.cazucito.threadsafecollections.format.ConsoleDemoResultFormatter;
import io.github.cazucito.threadsafecollections.format.DemoResultFormatter;
import io.github.cazucito.threadsafecollections.format.JsonDemoResultFormatter;
import java.io.PrintStream;
import java.util.List;

/**
 * Punto de entrada de la aplicación.
 */
public final class ThreadSafeCollectionsApplication {

    private static final double FAST_DELAY_MULTIPLIER = 0.25d;

    private ThreadSafeCollectionsApplication() {
    }

    /**
     * Ejecuta la aplicación principal.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    /**
     * Ejecuta la aplicación con flujos configurables para facilitar pruebas.
     *
     * @param args   argumentos de línea de comandos
     * @param output salida estándar
     * @param error  salida de error
     * @return código de salida
     */
    public static int run(String[] args, PrintStream output, PrintStream error) {
        CliOptions options = CliOptions.parse(args);
        if (options.errorMessage() != null) {
            error.println(options.errorMessage());
            printUsage(error);
            return 1;
        }

        DemoRegistry registry = DemoRegistry.defaultRegistry();
        DemoResultFormatter formatter = createFormatter(options.format(), options.debug());
        ConsolePrinter printer = new ConsolePrinter(output, options.debug(), formatter);
        double originalDelayMultiplier = ThreadPause.delayMultiplier();

        try {
            if (options.fast()) {
                ThreadPause.setDelayMultiplier(FAST_DELAY_MULTIPLIER);
            }

            if (options.help()) {
                printUsage(output);
                return 0;
            }

            if (options.list()) {
                formatter.formatDemoList(registry.demos(), output);
                return 0;
            }

            List<Demo> demosToRun;
            if (options.demoId() != null) {
                Demo selectedDemo = registry.findById(options.demoId())
                        .orElse(null);
                if (selectedDemo == null) {
                    error.println("No existe una demo con id '" + options.demoId() + "'.");
                    printUsage(error);
                    return 1;
                }
                demosToRun = List.of(selectedDemo);
            } else {
                demosToRun = registry.demos();
            }

            formatter.formatHeader("COLECCIONES SEGURAS EN AMBIENTES MULTIHILO", output);
            if (options.fast() && options.format() != OutputFormat.JSON) {
                formatter.formatInfo("Modo rápido activo para estudio guiado y CI.", output);
            }
            for (Demo demo : demosToRun) {
                formatter.formatDemoResult(demo, demo.run(), output);
            }
            formatter.formatFooter(output);
            return 0;
        } finally {
            ThreadPause.setDelayMultiplier(originalDelayMultiplier);
        }
    }

    private static void printUsage(PrintStream output) {
        output.println("Uso:");
        output.println("  ./mvnw exec:java -Dexec.args=\"--list\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--demo basic-collection\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--fast --demo basic-collection\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--debug --demo concurrent-hash-map\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--format json --demo basic-collection\"");
        output.println();
        output.println("Opciones:");
        output.println("  --list         Lista las demos disponibles.");
        output.println("  --demo <id>    Ejecuta solo la demo indicada.");
        output.println("  --fast         Reduce las pausas para clase, CI o revisión rápida.");
        output.println("  --debug        Muestra mensajes de depuración.");
        output.println("  --format <fmt> Formato de salida: console (default) o json.");
        output.println("  --help         Muestra esta ayuda.");
    }

    private static DemoResultFormatter createFormatter(OutputFormat format, boolean debug) {
        return switch (format) {
            case JSON -> new JsonDemoResultFormatter(debug);
            case CONSOLE -> new ConsoleDemoResultFormatter(debug);
        };
    }

    private record CliOptions(boolean debug, boolean list, boolean help, boolean fast, String demoId,
                              OutputFormat format, String errorMessage) {

        private static CliOptions parse(String[] args) {
            boolean debug = false;
            boolean list = false;
            boolean help = false;
            boolean fast = false;
            String demoId = null;
            OutputFormat format = OutputFormat.CONSOLE;

            for (int index = 0; index < args.length; index++) {
                String argument = args[index];
                switch (argument) {
                    case "--debug":
                        debug = true;
                        break;
                    case "--list":
                        list = true;
                        break;
                    case "--help":
                        help = true;
                        break;
                    case "--fast":
                        fast = true;
                        break;
                    case "--format":
                        if (index + 1 >= args.length) {
                            return new CliOptions(debug, list, help, fast, null, format,
                                    "La opción --format requiere un formato (console o json).");
                        }
                        String formatValue = args[++index].toLowerCase();
                        try {
                            format = OutputFormat.valueOf(formatValue.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            return new CliOptions(debug, list, help, fast, demoId, format,
                                    "Formato no soportado: " + formatValue + ". Use 'console' o 'json'.");
                        }
                        break;
                    case "--demo":
                        if (demoId != null) {
                            return new CliOptions(debug, list, help, fast, demoId, format,
                                    "Solo se puede especificar una demo por ejecución.");
                        }
                        if (index + 1 >= args.length) {
                            return new CliOptions(debug, list, help, fast, null, format,
                                    "La opción --demo requiere un identificador.");
                        }
                        demoId = args[++index];
                        break;
                    default:
                        return new CliOptions(debug, list, help, fast, demoId, format,
                                "Argumento no soportado: " + argument);
                }
            }

            if (list && demoId != null) {
                return new CliOptions(debug, list, help, fast, demoId, format,
                        "No se puede usar --list junto con --demo.");
            }

            return new CliOptions(debug, list, help, fast, demoId, format, null);
        }
    }

    private enum OutputFormat {
        CONSOLE,
        JSON
    }
}

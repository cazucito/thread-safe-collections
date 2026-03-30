package io.github.cazucito.threadsafecollections;

import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.cli.ConsolePrinter;
import io.github.cazucito.threadsafecollections.cli.MessageType;
import java.io.PrintStream;
import java.util.List;

/**
 * Punto de entrada de la aplicación.
 */
public final class ThreadSafeCollectionsApplication {

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
        ConsolePrinter printer = new ConsolePrinter(output, options.debug());

        if (options.help()) {
            printUsage(output);
            return 0;
        }

        if (options.list()) {
            printer.printDemoList(registry.demos());
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

        printer.print(MessageType.HEADER, "COLECCIONES SEGURAS EN AMBIENTES MULTIHILO");
        for (Demo demo : demosToRun) {
            printer.printDemoResult(demo.run());
        }
        printer.print(MessageType.FOOTER, "");
        return 0;
    }

    private static void printUsage(PrintStream output) {
        output.println("Uso:");
        output.println("  ./mvnw exec:java -Dexec.args=\"--list\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--demo basic-collection\"");
        output.println("  ./mvnw exec:java -Dexec.args=\"--debug --demo concurrent-hash-map\"");
        output.println();
        output.println("Opciones:");
        output.println("  --list         Lista las demos disponibles.");
        output.println("  --demo <id>    Ejecuta solo la demo indicada.");
        output.println("  --debug        Muestra mensajes de depuración.");
        output.println("  --help         Muestra esta ayuda.");
    }

    private record CliOptions(boolean debug, boolean list, boolean help, String demoId, String errorMessage) {

        private static CliOptions parse(String[] args) {
            boolean debug = false;
            boolean list = false;
            boolean help = false;
            String demoId = null;

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
                    case "--demo":
                        if (demoId != null) {
                            return new CliOptions(debug, list, help, demoId,
                                    "Solo se puede especificar una demo por ejecución.");
                        }
                        if (index + 1 >= args.length) {
                            return new CliOptions(debug, list, help, null,
                                    "La opción --demo requiere un identificador.");
                        }
                        demoId = args[++index];
                        break;
                    default:
                        return new CliOptions(debug, list, help, demoId,
                                "Argumento no soportado: " + argument);
                }
            }

            if (list && demoId != null) {
                return new CliOptions(debug, true, help, demoId,
                        "No se puede usar --list junto con --demo.");
            }

            return new CliOptions(debug, list, help, demoId, null);
        }
    }
}

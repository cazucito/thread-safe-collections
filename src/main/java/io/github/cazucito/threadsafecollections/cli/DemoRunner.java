package io.github.cazucito.threadsafecollections.cli;

import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.format.ConsoleDemoResultFormatter;
import io.github.cazucito.threadsafecollections.format.DemoResultFormatter;
import io.github.cazucito.threadsafecollections.format.JsonDemoResultFormatter;

/**
 * Ejecutor de demos via Maven.
 * 
 * Uso:
 * ./mvnw compile exec:java -Dexec.mainClass="...DemoRunner" -Dexec.args="basic-collection"
 * 
 * O con perfil (recomendado):
 * ./mvnw compile exec:java -Pdemo -Ddemo.id=basic-collection
 */
public final class DemoRunner {

    private DemoRunner() {
    }

    /**
     * Punto de entrada para ejecutar demos.
     *
     * @param args [demo-id] [--json] [--fast]
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String demoId = args[0];
        boolean json = false;
        boolean fast = false;

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--json":
                    json = true;
                    break;
                case "--fast":
                    fast = true;
                    break;
            }
        }

        int exitCode = runDemo(demoId, json, fast);
        System.exit(exitCode);
    }

    private static int runDemo(String demoId, boolean json, boolean fast) {
        DemoRegistry registry = DemoRegistry.defaultRegistry();
        DemoResultFormatter formatter = json 
            ? new JsonDemoResultFormatter(false)
            : new ConsoleDemoResultFormatter(false);
        ConsolePrinter printer = new ConsolePrinter(System.out, false, formatter);

        // Buscar exacto primero, luego fuzzy
        Demo demo = registry.findById(demoId).orElse(null);
        
        if (demo == null) {
            demo = registry.findUniqueMatch(demoId).orElse(null);
            if (demo != null) {
                System.out.println("Demo no encontrada exactamente. Usando: " + demo.id());
            }
        }

        if (demo == null) {
            System.err.println("Demo no encontrada: " + demoId);
            System.err.println();
            System.err.println(registry.getSuggestionsText(demoId));
            return 1;
        }

        if (fast) {
            io.github.cazucito.threadsafecollections.concurrency.ThreadPause.setDelayMultiplier(0.25d);
        }

        printer.printDemoResult(demo, demo.run());
        return 0;
    }

    private static void printUsage() {
        System.out.println("Uso: DemoRunner <demo-id> [--json] [--fast]");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  DemoRunner basic-collection");
        System.out.println("  DemoRunner concurrent-hash-map --json");
        System.out.println("  DemoRunner copy-on-write-array-list --fast");
        System.out.println();
        System.out.println("Demos disponibles:");
        DemoRegistry.defaultRegistry().demos().forEach(d -> 
            System.out.println("  - " + d.id())
        );
    }
}

package io.github.cazucito.threadsafecollections.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * Runner principal para ejecutar todos los benchmarks JMH.
 *
 * <p>Puede ejecutarse con: ./mvnw compile test-compile exec:java
 * -Dexec.mainClass="io.github.cazucito.threadsafecollections.benchmark.BenchmarkRunner"
 * -Dexec.classpathScope=test
 */
public class BenchmarkRunner {

    public static void main(String[] args) throws RunnerException {
        System.out.println("=".repeat(70));
        System.out.println("Thread-Safe Collections - JMH Benchmark Suite");
        System.out.println("=".repeat(70));
        System.out.println();

        Options options = new OptionsBuilder()
                // Incluir todas las clases que terminan en Benchmark
                .include(".*Benchmark")
                // Excluir la clase runner misma
                .exclude("BenchmarkRunner")
                // Configuración de warmup
                .warmupIterations(2)
                .warmupTime(TimeValue.seconds(1))
                // Configuración de medición
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(1))
                // Forks para evitar optimizaciones de JVM
                .forks(2)
                // Threads por benchmark
                .threads(1)
                // Modo de throughput
                .mode(org.openjdk.jmh.annotations.Mode.Throughput)
                // Unidad de tiempo
                .timeUnit(TimeUnit.MILLISECONDS)
                // Formato de salida
                .output("jmh-benchmark-results.txt")
                // Resultados JSON
                .result("jmh-benchmark-results.json")
                .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
                .build();

        new Runner(options).run();

        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("Benchmarks completados!");
        System.out.println("Resultados guardados en:");
        System.out.println("  - jmh-benchmark-results.txt (formato texto)");
        System.out.println("  - jmh-benchmark-results.json (formato JSON)");
        System.out.println("=".repeat(70));
    }
}

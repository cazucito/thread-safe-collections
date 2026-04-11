package io.github.cazucito.threadsafecollections.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarks de rendimiento para comparar implementaciones de List.
 *
 * <p>Compara ArrayList (no thread-safe), synchronizedList (wrapper thread-safe)
 * y CopyOnWriteArrayList (diseñado para lecturas frecuentes).
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class ListBenchmark {

    @Param({"1", "2", "4", "8"})
    private int threadCount;

    private List<String> arrayList;
    private List<String> synchronizedList;
    private List<String> copyOnWriteArrayList;

    private static final int INITIAL_SIZE = 1000;
    private static final String VALUE = "benchmark-value";

    @Setup
    public void setup() {
        arrayList = new ArrayList<>();
        synchronizedList = Collections.synchronizedList(new ArrayList<>());
        copyOnWriteArrayList = new CopyOnWriteArrayList<>();

        // Pre-poblar con datos iniciales
        for (int i = 0; i < INITIAL_SIZE; i++) {
            arrayList.add(VALUE + i);
            synchronizedList.add(VALUE + i);
            copyOnWriteArrayList.add(VALUE + i);
        }
    }

    @Benchmark
    @Group("arraylist_add")
    @GroupThreads(1)
    public void arrayListAdd(Blackhole blackhole) {
        boolean result = arrayList.add(VALUE);
        // Evitar crecimiento infinito
        if (arrayList.size() > INITIAL_SIZE * 2) {
            arrayList.subList(INITIAL_SIZE, arrayList.size()).clear();
        }
        blackhole.consume(result);
    }

    @Benchmark
    @Group("arraylist_get")
    @GroupThreads(1)
    public void arrayListGet(Blackhole blackhole) {
        int index = (int) (Math.random() * Math.min(arrayList.size(), INITIAL_SIZE));
        if (index < arrayList.size()) {
            String result = arrayList.get(index);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("arraylist_iterate")
    @GroupThreads(1)
    public void arrayListIterate(Blackhole blackhole) {
        for (String s : arrayList) {
            blackhole.consume(s);
        }
    }

    @Benchmark
    @Group("synchronizedlist_add")
    @GroupThreads(1)
    public void synchronizedListAdd(Blackhole blackhole) {
        boolean result = synchronizedList.add(VALUE);
        if (synchronizedList.size() > INITIAL_SIZE * 2) {
            synchronizedList.subList(INITIAL_SIZE, synchronizedList.size()).clear();
        }
        blackhole.consume(result);
    }

    @Benchmark
    @Group("synchronizedlist_get")
    @GroupThreads(1)
    public void synchronizedListGet(Blackhole blackhole) {
        int index = (int) (Math.random() * Math.min(synchronizedList.size(), INITIAL_SIZE));
        if (index < synchronizedList.size()) {
            String result = synchronizedList.get(index);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("synchronizedlist_iterate")
    @GroupThreads(1)
    public void synchronizedListIterate(Blackhole blackhole) {
        for (String s : synchronizedList) {
            blackhole.consume(s);
        }
    }

    @Benchmark
    @Group("copyonwritearraylist_add")
    @GroupThreads(1)
    public void copyOnWriteArrayListAdd(Blackhole blackhole) {
        boolean result = copyOnWriteArrayList.add(VALUE);
        if (copyOnWriteArrayList.size() > INITIAL_SIZE * 2) {
            copyOnWriteArrayList.subList(INITIAL_SIZE, copyOnWriteArrayList.size()).clear();
        }
        blackhole.consume(result);
    }

    @Benchmark
    @Group("copyonwritearraylist_get")
    @GroupThreads(1)
    public void copyOnWriteArrayListGet(Blackhole blackhole) {
        int index = (int) (Math.random() * Math.min(copyOnWriteArrayList.size(), INITIAL_SIZE));
        if (index < copyOnWriteArrayList.size()) {
            String result = copyOnWriteArrayList.get(index);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("copyonwritearraylist_iterate")
    @GroupThreads(1)
    public void copyOnWriteArrayListIterate(Blackhole blackhole) {
        for (String s : copyOnWriteArrayList) {
            blackhole.consume(s);
        }
    }
}

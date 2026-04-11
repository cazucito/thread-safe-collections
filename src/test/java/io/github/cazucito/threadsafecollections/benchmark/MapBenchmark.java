package io.github.cazucito.threadsafecollections.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarks de rendimiento para comparar implementaciones de Map.
 *
 * <p>Compara HashMap (no thread-safe), synchronizedMap (wrapper thread-safe)
 * y ConcurrentHashMap (diseñado para concurrencia).
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class MapBenchmark {

    @Param({"1", "2", "4", "8"})
    private int threadCount;

    private Map<Integer, String> hashMap;
    private Map<Integer, String> synchronizedMap;
    private Map<Integer, String> concurrentHashMap;

    private static final int INITIAL_SIZE = 1000;
    private static final String VALUE = "benchmark-value";

    @Setup
    public void setup() {
        hashMap = new HashMap<>();
        synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        concurrentHashMap = new ConcurrentHashMap<>();

        // Pre-poblar con datos iniciales
        for (int i = 0; i < INITIAL_SIZE; i++) {
            hashMap.put(i, VALUE);
            synchronizedMap.put(i, VALUE);
            concurrentHashMap.put(i, VALUE);
        }
    }

    @Benchmark
    @Group("hashmap")
    @GroupThreads(1)
    public void hashMapPut(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE * 2);
        String result = hashMap.put(key, VALUE);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("hashmap")
    @GroupThreads(1)
    public void hashMapGet(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE);
        String result = hashMap.get(key);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("synchronizedmap")
    @GroupThreads(1)
    public void synchronizedMapPut(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE * 2);
        String result = synchronizedMap.put(key, VALUE);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("synchronizedmap")
    @GroupThreads(1)
    public void synchronizedMapGet(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE);
        String result = synchronizedMap.get(key);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("concurrenthashmap")
    @GroupThreads(1)
    public void concurrentHashMapPut(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE * 2);
        String result = concurrentHashMap.put(key, VALUE);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("concurrenthashmap")
    @GroupThreads(1)
    public void concurrentHashMapGet(Blackhole blackhole) {
        int key = (int) (Math.random() * INITIAL_SIZE);
        String result = concurrentHashMap.get(key);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("hashmap_mixed")
    @GroupThreads(2)
    public void hashMapMixedReadWrite(Blackhole blackhole, ThreadState threadState) {
        if (threadState.threadId % 2 == 0) {
            // Writer thread
            int key = (int) (Math.random() * INITIAL_SIZE * 2);
            String result = hashMap.put(key, VALUE);
            blackhole.consume(result);
        } else {
            // Reader thread
            int key = (int) (Math.random() * INITIAL_SIZE);
            String result = hashMap.get(key);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("synchronizedmap_mixed")
    @GroupThreads(2)
    public void synchronizedMapMixedReadWrite(Blackhole blackhole, ThreadState threadState) {
        if (threadState.threadId % 2 == 0) {
            int key = (int) (Math.random() * INITIAL_SIZE * 2);
            String result = synchronizedMap.put(key, VALUE);
            blackhole.consume(result);
        } else {
            int key = (int) (Math.random() * INITIAL_SIZE);
            String result = synchronizedMap.get(key);
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("concurrenthashmap_mixed")
    @GroupThreads(2)
    public void concurrentHashMapMixedReadWrite(Blackhole blackhole, ThreadState threadState) {
        if (threadState.threadId % 2 == 0) {
            int key = (int) (Math.random() * INITIAL_SIZE * 2);
            String result = concurrentHashMap.put(key, VALUE);
            blackhole.consume(result);
        } else {
            int key = (int) (Math.random() * INITIAL_SIZE);
            String result = concurrentHashMap.get(key);
            blackhole.consume(result);
        }
    }

    @State(Scope.Thread)
    public static class ThreadState {
        private static int counter = 0;
        int threadId;

        @Setup
        public void setup() {
            threadId = counter++;
        }
    }
}

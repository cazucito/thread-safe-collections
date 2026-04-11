package io.github.cazucito.threadsafecollections.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Benchmarks de rendimiento para comparar contadores atómicos.
 *
 * <p>Compara AtomicLong (single CAS operation) vs LongAdder (striped counters)
 * bajo diferentes niveles de contención.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class CounterBenchmark {

    @Param({"1", "2", "4", "8"})
    private int threadCount;

    private AtomicLong atomicLong;
    private LongAdder longAdder;

    @Setup
    public void setup() {
        atomicLong = new AtomicLong(0);
        longAdder = new LongAdder();
    }

    @Benchmark
    @Group("atomiclong_increment")
    @GroupThreads(1)
    public void atomicLongIncrement(Blackhole blackhole) {
        long result = atomicLong.incrementAndGet();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("longadder_increment")
    @GroupThreads(1)
    public void longAdderIncrement(Blackhole blackhole) {
        longAdder.increment();
        // No retorna valor, consume la operación
        blackhole.consume(longAdder);
    }

    @Benchmark
    @Group("atomiclong_get")
    @GroupThreads(1)
    public void atomicLongGet(Blackhole blackhole) {
        long result = atomicLong.get();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("longadder_sum")
    @GroupThreads(1)
    public void longAdderSum(Blackhole blackhole) {
        long result = longAdder.sum();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("atomiclong_contention")
    @GroupThreads(2)
    public void atomicLongUnderContention(Blackhole blackhole) {
        long result = atomicLong.incrementAndGet();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("longadder_contention")
    @GroupThreads(2)
    public void longAdderUnderContention(Blackhole blackhole) {
        longAdder.increment();
        blackhole.consume(longAdder);
    }

    @Benchmark
    @Group("atomiclong_high_contention")
    @GroupThreads(4)
    public void atomicLongUnderHighContention(Blackhole blackhole) {
        long result = atomicLong.incrementAndGet();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("longadder_high_contention")
    @GroupThreads(4)
    public void longAdderUnderHighContention(Blackhole blackhole) {
        longAdder.increment();
        blackhole.consume(longAdder);
    }

    @Benchmark
    @Group("atomiclong_mixed")
    @GroupThreads(2)
    public void atomicLongMixed(Blackhole blackhole, ThreadState threadState) {
        if (threadState.threadId % 2 == 0) {
            // Writer
            long result = atomicLong.incrementAndGet();
            blackhole.consume(result);
        } else {
            // Reader
            long result = atomicLong.get();
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("longadder_mixed")
    @GroupThreads(2)
    public void longAdderMixed(Blackhole blackhole, ThreadState threadState) {
        if (threadState.threadId % 2 == 0) {
            // Writer
            longAdder.increment();
            blackhole.consume(longAdder);
        } else {
            // Reader
            long result = longAdder.sum();
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

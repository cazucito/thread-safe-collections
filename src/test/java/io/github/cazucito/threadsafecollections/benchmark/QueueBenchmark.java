package io.github.cazucito.threadsafecollections.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarks de rendimiento para comparar implementaciones de Queue.
 *
 * <p>Compara LinkedList (no thread-safe para concurrencia),
 * ConcurrentLinkedQueue (lock-free) y ArrayBlockingQueue (bounded, blocking).
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class QueueBenchmark {

    @Param({"1", "2", "4", "8"})
    private int threadCount;

    private Queue<String> linkedList;
    private Queue<String> concurrentLinkedQueue;
    private ArrayBlockingQueue<String> arrayBlockingQueue;

    private static final int QUEUE_CAPACITY = 10000;
    private static final String VALUE = "benchmark-value";

    @Setup
    public void setup() {
        linkedList = new LinkedList<>();
        concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        arrayBlockingQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        // Pre-poblar con datos iniciales (solo para queues bounded)
        for (int i = 0; i < QUEUE_CAPACITY / 2; i++) {
            concurrentLinkedQueue.offer(VALUE + i);
        }
    }

    @Benchmark
    @Group("linkedlist_offer")
    @GroupThreads(1)
    public void linkedListOffer(Blackhole blackhole) {
        boolean result = linkedList.offer(VALUE);
        // Evitar crecimiento infinito
        if (linkedList.size() > QUEUE_CAPACITY) {
            linkedList.poll();
        }
        blackhole.consume(result);
    }

    @Benchmark
    @Group("linkedlist_poll")
    @GroupThreads(1)
    public void linkedListPoll(Blackhole blackhole) {
        // Asegurar que hay elementos
        if (linkedList.isEmpty()) {
            linkedList.offer(VALUE);
        }
        String result = linkedList.poll();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("concurrentlinkedqueue_offer")
    @GroupThreads(1)
    public void concurrentLinkedQueueOffer(Blackhole blackhole) {
        boolean result = concurrentLinkedQueue.offer(VALUE);
        if (concurrentLinkedQueue.size() > QUEUE_CAPACITY) {
            concurrentLinkedQueue.poll();
        }
        blackhole.consume(result);
    }

    @Benchmark
    @Group("concurrentlinkedqueue_poll")
    @GroupThreads(1)
    public void concurrentLinkedQueuePoll(Blackhole blackhole) {
        if (concurrentLinkedQueue.isEmpty()) {
            concurrentLinkedQueue.offer(VALUE);
        }
        String result = concurrentLinkedQueue.poll();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("arrayblockingqueue_offer")
    @GroupThreads(1)
    public void arrayBlockingQueueOffer(Blackhole blackhole) {
        // ArrayBlockingQueue es bounded, puede rechazar si está llena
        boolean result = arrayBlockingQueue.offer(VALUE);
        blackhole.consume(result);
    }

    @Benchmark
    @Group("arrayblockingqueue_poll")
    @GroupThreads(1)
    public void arrayBlockingQueuePoll(Blackhole blackhole) {
        if (arrayBlockingQueue.isEmpty()) {
            arrayBlockingQueue.offer(VALUE);
        }
        String result = arrayBlockingQueue.poll();
        blackhole.consume(result);
    }

    @Benchmark
    @Group("linkedlist_producer_consumer")
    @GroupThreads(2)
    public void linkedListProducerConsumer(Blackhole blackhole, ThreadState threadState) {
        if (threadState.isProducer) {
            boolean result = linkedList.offer(VALUE);
            if (linkedList.size() > QUEUE_CAPACITY) {
                linkedList.poll();
            }
            blackhole.consume(result);
        } else {
            if (linkedList.isEmpty()) {
                linkedList.offer(VALUE);
            }
            String result = linkedList.poll();
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("concurrentlinkedqueue_producer_consumer")
    @GroupThreads(2)
    public void concurrentLinkedQueueProducerConsumer(Blackhole blackhole, ThreadState threadState) {
        if (threadState.isProducer) {
            boolean result = concurrentLinkedQueue.offer(VALUE);
            if (concurrentLinkedQueue.size() > QUEUE_CAPACITY) {
                concurrentLinkedQueue.poll();
            }
            blackhole.consume(result);
        } else {
            if (concurrentLinkedQueue.isEmpty()) {
                concurrentLinkedQueue.offer(VALUE);
            }
            String result = concurrentLinkedQueue.poll();
            blackhole.consume(result);
        }
    }

    @Benchmark
    @Group("arrayblockingqueue_producer_consumer")
    @GroupThreads(2)
    public void arrayBlockingQueueProducerConsumer(Blackhole blackhole, ThreadState threadState) {
        if (threadState.isProducer) {
            boolean result = arrayBlockingQueue.offer(VALUE);
            blackhole.consume(result);
        } else {
            if (arrayBlockingQueue.isEmpty()) {
                arrayBlockingQueue.offer(VALUE);
            }
            String result = arrayBlockingQueue.poll();
            blackhole.consume(result);
        }
    }

    @State(Scope.Thread)
    public static class ThreadState {
        private static int counter = 0;
        boolean isProducer;

        @Setup
        public void setup() {
            isProducer = (counter++ % 2) == 0;
        }
    }
}

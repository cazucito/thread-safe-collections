package io.github.cazucito.threadsafecollections.concurrency;

/**
 * Estado final de una ejecución asíncrona.
 */
public enum CompletionStatus {
    COMPLETED,
    TIMED_OUT,
    INTERRUPTED
}

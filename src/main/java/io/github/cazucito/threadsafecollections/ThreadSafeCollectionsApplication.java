package io.github.cazucito.threadsafecollections;

import io.github.cazucito.threadsafecollections.demo.BasicCollectionDemo;
import io.github.cazucito.threadsafecollections.demo.ConcurrentHashMapDemo;
import io.github.cazucito.threadsafecollections.demo.ConcurrentSkipListMapDemo;
import io.github.cazucito.threadsafecollections.demo.ConcurrentSkipListSetDemo;
import io.github.cazucito.threadsafecollections.demo.CopyOnWriteArrayListDemo;
import io.github.cazucito.threadsafecollections.demo.CopyOnWriteArraySetDemo;
import io.github.cazucito.threadsafecollections.support.ConsolePrinter;
import io.github.cazucito.threadsafecollections.support.MessageType;

/**
 * Punto de entrada de la aplicación.
 */
public final class ThreadSafeCollectionsApplication {

    private ThreadSafeCollectionsApplication() {
    }

    /**
     * Ejecuta todas las demostraciones disponibles.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        ConsolePrinter.enableDebug();
        ConsolePrinter.print(MessageType.HEADER, "COLECCIONES SEGURAS EN AMBIENTES MULTIHILO");

        ConsolePrinter.print(MessageType.TITLE, "COLECCIONES TRADICIONALES (java.util)");
        BasicCollectionDemo.demonstrateUnsynchronizedAddition();
        BasicCollectionDemo.demonstrateSynchronizedAddition();
        BasicCollectionDemo.demonstrateUtilitySynchronizedAddition();

        ConsolePrinter.print(MessageType.TITLE, "CopyOnWriteArrayList (java.util.concurrent)");
        CopyOnWriteArrayListDemo.showConstructors();
        CopyOnWriteArrayListDemo.demonstrateConcurrentIteration();

        ConsolePrinter.print(MessageType.TITLE, "CopyOnWriteArraySet (java.util.concurrent)");
        CopyOnWriteArraySetDemo.showConstructors();
        CopyOnWriteArraySetDemo.demonstrateConcurrentIteration();

        ConsolePrinter.print(MessageType.TITLE, "ConcurrentSkipListSet (java.util.concurrent)");
        ConcurrentSkipListSetDemo.showConstructors();
        ConcurrentSkipListSetDemo.demonstrateConcurrentIteration();

        ConsolePrinter.print(MessageType.TITLE, "ConcurrentSkipListMap (java.util.concurrent)");
        ConcurrentSkipListMapDemo.showConstructors();
        ConcurrentSkipListMapDemo.demonstrateConcurrentIteration();

        ConsolePrinter.print(MessageType.TITLE, "ConcurrentHashMap (java.util.concurrent)");
        ConcurrentHashMapDemo.showConstructors();
        ConcurrentHashMapDemo.demonstrateConcurrentIteration();

        ConsolePrinter.print(MessageType.FOOTER, "");
    }
}

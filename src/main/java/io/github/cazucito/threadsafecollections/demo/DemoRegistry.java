package io.github.cazucito.threadsafecollections.demo;

import java.util.List;
import java.util.Optional;

/**
 * Registro central de demos disponibles para la CLI y la aplicación.
 */
public final class DemoRegistry {

    private final List<Demo> demos;

    private DemoRegistry(List<Demo> demos) {
        this.demos = List.copyOf(demos);
    }

    /**
     * Crea el registro por defecto de la aplicación.
     *
     * @return registro con todas las demos soportadas
     */
    public static DemoRegistry defaultRegistry() {
        return new DemoRegistry(List.of(
                new BasicCollectionDemo(),
                new CopyOnWriteArrayListDemo(),
                new CopyOnWriteArraySetDemo(),
                new ConcurrentSkipListSetDemo(),
                new ConcurrentSkipListMapDemo(),
                new ConcurrentHashMapDemo()
        ));
    }

    /**
     * Devuelve todas las demos registradas.
     *
     * @return demos disponibles
     */
    public List<Demo> demos() {
        return demos;
    }

    /**
     * Busca una demo por identificador.
     *
     * @param demoId identificador buscado
     * @return demo encontrada si existe
     */
    public Optional<Demo> findById(String demoId) {
        return demos.stream()
                .filter(demo -> demo.id().equals(demoId))
                .findFirst();
    }
}

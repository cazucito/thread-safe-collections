package io.github.cazucito.threadsafecollections.demo;

import java.util.ArrayList;
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
                new ConcurrentHashMapDemo(),
                new ConcurrentLinkedQueueDemo(),
                new ArrayBlockingQueueDemo(),
                new LongAdderDemo()
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

    /**
     * Busca demos que coincidan parcialmente con el identificador proporcionado.
     * Realiza búsqueda fuzzy (case-insensitive, contiene substring).
     *
     * @param partialId fragmento del identificador buscado
     * @return lista de demos que coinciden parcialmente
     */
    public List<Demo> findByPartialId(String partialId) {
        String lowerPartial = partialId.toLowerCase();
        return demos.stream()
                .filter(demo -> demo.id().toLowerCase().contains(lowerPartial))
                .toList();
    }

    /**
     * Busca demos por coincidencia fuzzy incluyendo títulos.
     *
     * @param query término de búsqueda
     * @return lista de demos que coinciden en id o título
     */
    public List<Demo> fuzzySearch(String query) {
        String lowerQuery = query.toLowerCase();
        return demos.stream()
                .filter(demo -> 
                    demo.id().toLowerCase().contains(lowerQuery) ||
                    demo.title().toLowerCase().contains(lowerQuery))
                .toList();
    }

    /**
     * Encuentra la mejor coincidencia para un identificador parcial.
     * Si hay una única coincidencia, la devuelve.
     * Si hay múltiples coincidencias, devuelve empty.
     *
     * @param partialId fragmento del identificador
     * @return única demo coincidente, o empty si hay 0 o múltiples
     */
    public Optional<Demo> findUniqueMatch(String partialId) {
        List<Demo> matches = findByPartialId(partialId);
        return matches.size() == 1 ? Optional.of(matches.get(0)) : Optional.empty();
    }

    /**
     * Obtiene una lista de sugerencias formateadas para mostrar al usuario.
     *
     * @param query término de búsqueda que produjo múltiples resultados
     * @return texto con sugerencias formateadas
     */
    public String getSuggestionsText(String query) {
        List<Demo> matches = findByPartialId(query);
        
        if (matches.isEmpty()) {
            return "No se encontraron demos que coincidan con '" + query + "'.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Múltiples demos coinciden con '").append(query).append("':\n\n");
        
        for (Demo demo : matches) {
            sb.append("  • ").append(demo.id());
            sb.append(" - ").append(demo.title()).append("\n");
        }
        
        sb.append("\nUse el id completo con: --demo <id>");
        return sb.toString();
    }
}

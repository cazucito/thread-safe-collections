package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.ThreadPause;
import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoMessage;
import io.github.cazucito.threadsafecollections.demo.DemoRegistry;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * TR-1.2: Suite de Integración Concurrente.
 * 
 * Tests parametrizados que validan que todas las demos:
 * - Capturan correctamente DemoResult
 * - Verifican mensajes específicos (fail-fast vs fail-safe)
 * - No se rompen al refactorizar
 */
@DisplayName("TR-1.2: Suite de Integración Concurrente")
class DemoIntegrationTest {

    @BeforeEach
    void setFastMode() {
        ThreadPause.setDelayMultiplier(0.01d);
    }

    @AfterEach
    void resetDelayMultiplier() {
        ThreadPause.setDelayMultiplier(1.0d);
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "basic-collection",
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Cada demo produce resultado válido con metadata completa")
    void eachDemoProducesValidResultWithCompleteMetadata(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        assertAll("Validación de resultado para demo: " + demoId,
            () -> assertEquals(demoId, result.demoId(), "El ID debería coincidir"),
            () -> assertEquals(demo.title(), result.title(), "El título debería coincidir"),
            () -> assertFalse(result.messages().isEmpty(), "Debería tener mensajes"),
            () -> assertFalse(demo.learningObjective().isBlank(), "Debería tener objetivo de aprendizaje"),
            () -> assertFalse(demo.expectedObservation().isBlank(), "Debería tener observación esperada"),
            () -> assertFalse(demo.keyTakeaway().isBlank(), "Debería tener conclusión")
        );
    }

    @ParameterizedTest(name = "Demo de fail-fast: {0}")
    @CsvSource({
        "copy-on-write-array-list, ArrayList - FAIL-FAST",
        "copy-on-write-array-list, CopyOnWriteArrayList - FAIL-SAFE",
        "copy-on-write-array-set, HashSet - FAIL-FAST",
        "copy-on-write-array-set, CopyOnWriteArraySet - FAIL-SAFE",
        "concurrent-skip-list-set, SortedSet - FAIL-FAST",
        "concurrent-skip-list-set, ConcurrentSkipListSet - FAIL-SAFE",
        "concurrent-skip-list-map, TreeMap - FAIL-FAST",
        "concurrent-skip-list-map, ConcurrentSkipListMap - FAIL-SAFE",
        "concurrent-hash-map, HashMap - FAIL-FAST",
        "concurrent-hash-map, ConcurrentHashMap - FAIL-SAFE"
    })
    @DisplayName("Demos de comparación incluyen subtítulos FAIL-FAST y FAIL-SAFE")
    void comparisonDemosIncludeFailFastAndFailSafeSubtitles(String demoId, String expectedSubtitle) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        boolean hasSubtitle = result.messages().stream()
                .filter(m -> m.type() == MessageType.SUBTITLE)
                .map(DemoMessage::text)
                .anyMatch(text -> text.contains(expectedSubtitle));

        assertTrue(hasSubtitle, 
            "La demo '" + demoId + "' debería contener subtítulo: '" + expectedSubtitle + "'");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Demos de colecciones concurrentes muestran excepción en versión no segura")
    void concurrentCollectionDemosShowExceptionInUnsafeVersion(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        // Verificar que hay al menos un mensaje de tipo EXCEPTION o ERROR
        boolean hasExceptionMessage = result.messages().stream()
                .anyMatch(m -> m.type() == MessageType.EXCEPTION || m.type() == MessageType.ERROR);

        assertTrue(hasExceptionMessage,
            "La demo '" + demoId + "' debería mostrar excepción/error en la versión no segura");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "basic-collection"
    })
    @DisplayName("Demo de colecciones básicas muestra éxito o error de lógica")
    void basicCollectionDemoShowsSuccessOrLogicError(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        // Verificar que hay mensajes de éxito o error de lógica
        boolean hasSuccessOrLogicError = result.messages().stream()
                .anyMatch(m -> m.type() == MessageType.SUCCESS || m.type() == MessageType.LOGIC_ERROR);

        assertTrue(hasSuccessOrLogicError,
            "La demo '" + demoId + "' debería mostrar éxito o error de lógica");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Demos de colecciones concurrentes incluyen información de constructores")
    void concurrentCollectionDemosIncludeConstructorInformation(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        boolean hasConstructorInfo = result.messages().stream()
                .filter(m -> m.type() == MessageType.SUBTITLE)
                .map(DemoMessage::text)
                .anyMatch(text -> text.contains("CONSTRUCTORES"));

        assertTrue(hasConstructorInfo,
            "La demo '" + demoId + "' debería incluir sección de CONSTRUCTORES");
    }

    @Test
    @DisplayName("Demo de colecciones básicas incluye subtítulos de sincronización")
    void basicCollectionDemoIncludesSynchronizationSubtitles() {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById("basic-collection")
                .orElseThrow(() -> new AssertionError("Demo no encontrada: basic-collection"));

        DemoResult result = demo.run();

        boolean hasUnsyncSubtitle = result.messages().stream()
                .filter(m -> m.type() == MessageType.SUBTITLE)
                .map(DemoMessage::text)
                .anyMatch(text -> text.contains("no sincronizado") || text.contains("synchronized"));

        assertTrue(hasUnsyncSubtitle,
            "La demo 'basic-collection' debería incluir subtítulos de sincronización");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Demos de colecciones concurrentes terminan con mensaje de estado final")
    void concurrentCollectionDemosEndWithFinalStateMessage(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        List<DemoMessage> messages = result.messages();
        assertFalse(messages.isEmpty(), "La demo debería tener mensajes");

        // Verificar que hay al menos un mensaje con "Estado final"
        boolean hasFinalState = messages.stream()
                .filter(m -> m.type() == MessageType.MESSAGE)
                .map(DemoMessage::text)
                .anyMatch(text -> text.contains("Estado final"));

        assertTrue(hasFinalState,
            "La demo '" + demoId + "' debería terminar con mensaje de 'Estado final'");
    }

    @Test
    @DisplayName("Demo de colecciones básicas termina con resultados de tamaño")
    void basicCollectionDemoEndsWithSizeResults() {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById("basic-collection")
                .orElseThrow(() -> new AssertionError("Demo no encontrada: basic-collection"));

        DemoResult result = demo.run();

        List<DemoMessage> messages = result.messages();
        assertFalse(messages.isEmpty(), "La demo debería tener mensajes");

        // Verificar que hay mensajes de éxito o error de lógica (que indican resultado del tamaño)
        boolean hasSizeResult = messages.stream()
                .anyMatch(m -> m.type() == MessageType.SUCCESS || m.type() == MessageType.LOGIC_ERROR);

        assertTrue(hasSizeResult,
            "La demo 'basic-collection' debería terminar con resultados de tamaño (SUCCESS o LOGIC_ERROR)");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "basic-collection",
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Mensajes de demo son inmutables después de construir")
    void demoMessagesAreImmutableAfterBuild(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();
        List<DemoMessage> messages = result.messages();

        // Verificar que la lista de mensajes es inmutable
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
            () -> messages.add(new DemoMessage(MessageType.DEBUG, "test")),
            "La lista de mensajes debería ser inmutable");
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "basic-collection",
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Cada demo produce mensajes con tipos válidos")
    void eachDemoProducesMessagesWithValidTypes(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result = demo.run();

        Set<MessageType> validTypes = java.util.EnumSet.allOf(MessageType.class);

        for (DemoMessage message : result.messages()) {
            assertNotNull(message.type(), "El tipo de mensaje no debería ser null");
            assertTrue(validTypes.contains(message.type()),
                "Tipo de mensaje inválido: " + message.type());
            assertNotNull(message.text(), "El texto del mensaje no debería ser null");
        }
    }

    @ParameterizedTest(name = "Demo: {0}")
    @ValueSource(strings = {
        "basic-collection",
        "copy-on-write-array-list",
        "copy-on-write-array-set",
        "concurrent-skip-list-set",
        "concurrent-skip-list-map",
        "concurrent-hash-map"
    })
    @DisplayName("Resultado de demo mantiene consistencia entre ejecuciones")
    void demoResultMaintainsConsistencyBetweenRuns(String demoId) {
        Demo demo = DemoRegistry.defaultRegistry()
                .findById(demoId)
                .orElseThrow(() -> new AssertionError("Demo no encontrada: " + demoId));

        DemoResult result1 = demo.run();
        DemoResult result2 = demo.run();

        // Verificar que IDs y títulos son consistentes
        assertAll("Consistencia entre ejecuciones",
            () -> assertEquals(result1.demoId(), result2.demoId(), "ID debería ser consistente"),
            () -> assertEquals(result1.title(), result2.title(), "Título debería ser consistente"),
            () -> assertEquals(result1.messages().size(), result2.messages().size(),
                "Cantidad de mensajes debería ser consistente")
        );
    }
}
